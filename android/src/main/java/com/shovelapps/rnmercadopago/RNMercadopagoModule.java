package com.shovelapps.rnmercadopago;


import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;


import com.google.gson.Gson;
import com.mercadopago.callbacks.Callback;
import com.mercadopago.constants.PaymentMethods;
import com.mercadopago.constants.PaymentTypes;
import com.mercadopago.constants.Sites;
import com.mercadopago.core.MercadoPago;
import com.mercadopago.core.MerchantServer;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.model.ApiException;

import com.mercadopago.model.DecorationPreference;
import com.mercadopago.model.Installment;
import com.mercadopago.model.Issuer;
import com.mercadopago.model.Item;
import com.mercadopago.model.MerchantPayment;
import com.mercadopago.model.BankDeal;
import com.mercadopago.model.CardToken;
import com.mercadopago.model.IdentificationType;
import com.mercadopago.model.Instruction;
import com.mercadopago.model.PayerCost;
import com.mercadopago.model.Payment;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.PaymentPreference;
import com.mercadopago.model.PaymentResult;
import com.mercadopago.model.Token;

import com.mercadopago.util.JsonUtil;

import com.mercadopago.util.MercadoPagoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.facebook.react.bridge.*;


public class RNMercadopagoModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    private CallbackContext callback = null;

    public RNMercadopagoModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(this);
    }

    @Override
     public String getName() {
            return "RNMercadopago";
     }

    @ReactMethod
     public void startCheckout(ReadableArray args, com.facebook.react.bridge.Callback success, com.facebook.react.bridge.Callback error) {
            String action = "startCheckout";
            try {
                this.execute(action, JsonConvert.reactToJSON(args), new CallbackContext(success, error));
            } catch (Exception ex) {

            }
     }

    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {


        if (action.equals("setPaymentPreference")){
            PaymentPreference paymentPreference = new PaymentPreference();
            if (data.getInt(0) != 0) {
                paymentPreference.setMaxAcceptedInstallments(data.getInt(0));
            }
            if (data.getInt(1) != 0){
                paymentPreference.setDefaultInstallments(data.getInt(1));
            }
            List<String> excludedPaymentTypes = new ArrayList();
            JSONArray paymentTypesJson = data.getJSONArray(3);
            for (int i = 0; i<paymentTypesJson.length(); i++ ){
                excludedPaymentTypes.add(paymentTypesJson.getString(i));
            }

            paymentPreference.setExcludedPaymentTypeIds(excludedPaymentTypes);

            List<String> excludedPaymentMethods = new ArrayList();
            JSONArray paymentMethodJson = data.getJSONArray(2);
            for (int i = 0; i<paymentMethodJson.length(); i++ ){
                excludedPaymentMethods.add(paymentMethodJson.getString(i));
            }

            paymentPreference.setExcludedPaymentMethodIds(excludedPaymentMethods);
            callbackContext.success(JsonUtil.getInstance().toJson(paymentPreference));

            return true;

        } else if (action.equals("startCheckout")) {
            DecorationPreference decorationPreference = new DecorationPreference();
            
            if (data.getString(2) != "null") {
                decorationPreference.setBaseColor(data.getString(2));
                
            }
            if (data.getBoolean(3)) {
                decorationPreference.enableDarkFont();
            }

            new MercadoPago.StartActivityBuilder()
                    .setActivity(getCurrentActivity())
                    .setDecorationPreference(decorationPreference)
                    .setPublicKey(data.getString(0))
                    .setCheckoutPreferenceId(data.getString(1))
                    .startCheckoutActivity();

            
            callback = callbackContext;
            
            
            return true;
            
            
        } else if (action.equals("showPaymentVault")){
            
            DecorationPreference decorationPreference = new DecorationPreference();
            
            if (data.getString(3) != "null") {
                decorationPreference.setBaseColor(data.getString(3));
                
            }
            if (data.getBoolean(4)){
                decorationPreference.enableDarkFont();
            }

            callback = callbackContext;
            BigDecimal amount = new BigDecimal(data.getInt(2));
            MercadoPago.StartActivityBuilder mp = new MercadoPago.StartActivityBuilder()
                    .setActivity(getCurrentActivity())
                    .setPublicKey(data.getString(0))
                    .setDecorationPreference(decorationPreference)
                    .setPaymentPreference(JsonUtil.getInstance().fromJson(data.getString(5),PaymentPreference.class))
                    .setAmount(amount);


            if (data.getString(1).toUpperCase().equals("ARGENTINA")){
                mp.setSite(Sites.ARGENTINA);
            } else if (data.getString(1).toUpperCase().equals("BRASIL")){
                mp.setSite(Sites.BRASIL);
            } else if (data.getString(1).toUpperCase().equals("CHILE")){
                mp.setSite(Sites.CHILE);
            } else if (data.getString(1).toUpperCase().equals("COLOMBIA")){
                mp.setSite(Sites.COLOMBIA);
            } else if (data.getString(1).toUpperCase().equals("MEXICO")){
                mp.setSite(Sites.MEXICO);
            } else if (data.getString(1).toUpperCase().equals("USA")){
                mp.setSite(Sites.USA);
            } else if (data.getString(1).toUpperCase().equals("VENEZUELA")){
                mp.setSite(Sites.VENEZUELA);
            }

            mp.startPaymentVaultActivity();
            
            return true;
            
            
        } else if (action.equals("showCardWithoutInstallments")){
            
            DecorationPreference decorationPreference = new DecorationPreference();
            
            if (data.getString(1) != "null") {
                decorationPreference.setBaseColor(data.getString(1));
                
            }
            if (data.getBoolean(2)){
                decorationPreference.enableDarkFont();
            }
            callback = callbackContext;
            new MercadoPago.StartActivityBuilder()
                    .setActivity(getCurrentActivity())
                    .setDecorationPreference(decorationPreference)
                    .setPublicKey(data.getString(0))
                    .setPaymentPreference(JsonUtil.getInstance().fromJson(data.getString(3),PaymentPreference.class))
                    .setInstallmentsEnabled(false)
                    .startCardVaultActivity();
            
            return true;
            
        } else if (action.equals("showCardWithInstallments")){
            callback = callbackContext;
            
            DecorationPreference decorationPreference = new DecorationPreference();
            
            if (data.getString(3) != "null") {
                decorationPreference.setBaseColor(data.getString(3));
                
            }
            if (data.getBoolean(4)){
                decorationPreference.enableDarkFont();
            }
            
            BigDecimal amount = new BigDecimal(data.getInt(2));
            MercadoPago.StartActivityBuilder mp = new MercadoPago.StartActivityBuilder()
                .setActivity(getCurrentActivity())
                .setPublicKey(data.getString(0))
                .setDecorationPreference(decorationPreference)
                .setPaymentPreference(JsonUtil.getInstance().fromJson(data.getString(5),PaymentPreference.class))
                .setInstallmentsEnabled(true)
                .setAmount(amount);
            if (data.getString(1).toUpperCase().equals("ARGENTINA")){
                mp.setSite(Sites.ARGENTINA);
            } else if (data.getString(1).toUpperCase().equals("BRASIL")){
                mp.setSite(Sites.BRASIL);
            } else if (data.getString(1).toUpperCase().equals("CHILE")){
                mp.setSite(Sites.CHILE);
            } else if (data.getString(1).toUpperCase().equals("COLOMBIA")){
                mp.setSite(Sites.COLOMBIA);
            } else if (data.getString(1).toUpperCase().equals("MEXICO")){
                mp.setSite(Sites.MEXICO);
            } else if (data.getString(1).toUpperCase().equals("USA")){
                mp.setSite(Sites.USA);
            } else if (data.getString(1).toUpperCase().equals("VENEZUELA")){
                mp.setSite(Sites.VENEZUELA);
            }
            mp.startCardVaultActivity();
            
            return true;
            
        } else if (action.equals("showPaymentMethods")){
            callback = callbackContext;
            DecorationPreference decorationPreference = new DecorationPreference();
            
            if (data.getString(1) != "null") {
                decorationPreference.setBaseColor(data.getString(1));
                
            }
            if (data.getBoolean(2)){
                decorationPreference.enableDarkFont();
            }
            
            new MercadoPago.StartActivityBuilder()
                .setActivity(getCurrentActivity())
                .setPublicKey(data.getString(0))
                .setDecorationPreference(decorationPreference)
                .setPaymentPreference(JsonUtil.getInstance().fromJson(data.getString(3),PaymentPreference.class))
                .startPaymentMethodsActivity();
            
            return true;
            
        } else if (action.equals("showIssuers")){
            callback = callbackContext;
            
            DecorationPreference decorationPreference = new DecorationPreference();
            
            if (data.getString(2) != "null") {
                decorationPreference.setBaseColor(data.getString(2));
                
            }
            if (data.getBoolean(3)){
                decorationPreference.enableDarkFont();
            }
            PaymentMethod paymentMethod = new PaymentMethod();
            paymentMethod.setId(data.getString(1));
            
            new MercadoPago.StartActivityBuilder()
                .setActivity(getCurrentActivity())
                .setPublicKey(data.getString(0))
                .setDecorationPreference(decorationPreference)
                .setPaymentMethod(paymentMethod)
                .startIssuersActivity();

            return true;
            
        } else if (action.equals("showInstallments")){
            callback = callbackContext;
            DecorationPreference decorationPreference = new DecorationPreference();
            
            if (data.getString(5) != "null") {
                decorationPreference.setBaseColor(data.getString(5));
                
            }
            if (data.getBoolean(6)){
                decorationPreference.enableDarkFont();
            }
            
            PaymentMethod paymentMethod = new PaymentMethod();
            paymentMethod.setId(data.getString(3));
            Issuer issuer = new Issuer();
            issuer.setId(data.getLong(4));
            BigDecimal amount = new BigDecimal(data.getInt(2));
            
            MercadoPago.StartActivityBuilder mp = new MercadoPago.StartActivityBuilder()
                    .setActivity(getCurrentActivity())
                    .setPublicKey(data.getString(0))
                    .setDecorationPreference(decorationPreference)
                    .setPaymentPreference(JsonUtil.getInstance().fromJson(data.getString(7),PaymentPreference.class))
                    .setAmount(amount)
                    .setIssuer(issuer)
                    .setPaymentMethod(paymentMethod);
            if (data.getString(1).toUpperCase().equals("ARGENTINA")){
                mp.setSite(Sites.ARGENTINA);
            } else if (data.getString(1).toUpperCase().equals("BRASIL")){
                mp.setSite(Sites.BRASIL);
            } else if (data.getString(1).toUpperCase().equals("CHILE")){
                mp.setSite(Sites.CHILE);
            } else if (data.getString(1).toUpperCase().equals("COLOMBIA")){
                mp.setSite(Sites.COLOMBIA);
            } else if (data.getString(1).toUpperCase().equals("MEXICO")){
                mp.setSite(Sites.MEXICO);
            } else if (data.getString(1).toUpperCase().equals("USA")){
                mp.setSite(Sites.USA);
            } else if (data.getString(1).toUpperCase().equals("VENEZUELA")){
                mp.setSite(Sites.VENEZUELA);
            }
            mp.startInstallmentsActivity();
            
            return true;
        } else {
            if (action.equals("showBankDeals")) {
                callback = callbackContext;
                
                DecorationPreference decorationPreference = new DecorationPreference();
                
                if (data.getString(1) != "null") {
                    decorationPreference.setBaseColor(data.getString(1));
                    
                }
                if (data.getBoolean(2)) {
                    decorationPreference.enableDarkFont();
                }
                
                new MercadoPago.StartActivityBuilder()
                        .setActivity(getCurrentActivity())
                        .setPublicKey(data.getString(0))
                        .setDecorationPreference(decorationPreference)
                        .startBankDealsActivity();
                
                return true;

            } else if (action.equals("showPaymentResult")) {
                callback = callbackContext;

                Payment payment =JsonUtil.getInstance().fromJson(data.getString(1), Payment.class);
                PaymentMethod paymentMethod = new PaymentMethod();
                paymentMethod.setPaymentTypeId(data.getString(2));

                new MercadoPago.StartActivityBuilder()
                .setActivity(getCurrentActivity())
                .setPublicKey(data.getString(0))
                .setPayment(payment)
                .setPaymentMethod(paymentMethod)
                .startPaymentResultActivity();
                
                return true;
            } else if (action.equals("createPayment")) {
                callback = callbackContext;
                
                final PaymentMethod paymentMethod = new PaymentMethod();
                paymentMethod.setId(data.getString(8));
                int installments = data.getInt(9);
                Long cardIssuerId = data.getLong(10);
                String token = data.getString(11);
                
                BigDecimal amount = new BigDecimal(data.getInt(3));
                if (paymentMethod != null) {
                    
                    Item item = new Item(data.getString(1), data.getInt(2), amount);
                    
                    String paymentMethodId = paymentMethod.getId();
                    
                    MerchantPayment payment = new MerchantPayment(item, installments,
                                                                  cardIssuerId, token, paymentMethodId, data.getLong(4), data.getString(5));
                    
                    // Enviar los datos a tu servidor
                    MerchantServer.createPayment(getCurrentActivity(), data.getString(6), data.getString(7), payment, new Callback<Payment>() {
                        @Override
                        public void success(Payment payment) {
                            
                            if (MercadoPagoUtil.isCard(paymentMethod.getPaymentTypeId())) {
                                Gson gson = new Gson();
                                String mpPayment = gson.toJson(payment);
                                String mpPaymentMethod = gson.toJson(paymentMethod);
                                JSONObject js = new JSONObject();
                                try {
                                    js.put("payment", mpPayment);
                                    js.put("payment_methods", mpPaymentMethod);
                                } catch (JSONException e) {
                                    
                                    e.printStackTrace();
                                }
                                callback.success(js.toString());
                            } else {
                                
                                Gson gson = new Gson();
                                String mpPayment = gson.toJson(payment);
                                String mpPaymentMethod = gson.toJson(paymentMethod);
                                JSONObject js = new JSONObject();
                                try {
                                    js.put("payment", mpPayment);
                                    js.put("payment_methods", mpPaymentMethod);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                callback.success(js.toString());
                            }
                        }
                        
                        @Override
                        public void failure(ApiException apiException) {
                            callback.success(apiException.getError());
                            
                        }
                    });
                } else {
                    Toast.makeText(getCurrentActivity(), "Invalid payment method", Toast.LENGTH_LONG).show();
                }
                
                return true;
                
            } else if (action.equals("getPaymentMethods")) {
                callback = callbackContext;
                
                MercadoPago mercadoPago = new MercadoPago.Builder()
                .setContext(getCurrentActivity())
                .setPublicKey(data.getString(0))
                .build();
                
                mercadoPago.getPaymentMethods(new Callback<List<PaymentMethod>>() {
                    @Override
                    public void success(List<PaymentMethod> paymentMethods) {
                        Gson gson = new Gson();
                        String pm = gson.toJson(paymentMethods);
                        callback.success(pm);
                    }
                    
                    @Override
                    public void failure(ApiException error) {
                        callback.error(error.toString());
                    }
                });
                return true;
            } else if (action.equals("getIssuers")) {
                callback = callbackContext;
                
                String paymentMethodId = data.getString(1);
                String bin = data.getString(2);
                
                MercadoPago mercadoPago = new MercadoPago.Builder()
                .setContext(getCurrentActivity())
                .setPublicKey(data.getString(0))
                .build();
                
                mercadoPago.getIssuers(paymentMethodId, bin, new Callback<List<Issuer>>() {
                    @Override
                    public void success(List<Issuer> issuers) {
                        Gson gson = new Gson();
                        String issuer = gson.toJson(issuers);
                        callback.success(issuer);
                    }
                    
                    @Override
                    public void failure(ApiException error) {
                        callback.error(error.toString());
                    }
                });
                return true;
            } else if (action.equals("getInstallments")) {
                callback = callbackContext;
                
                String paymentMethodId = data.getString(1);
                String bin = data.getString(2);
                Long issuerId = data.getLong(3);
                
                BigDecimal amount = new BigDecimal(data.getInt(4));
                
                MercadoPago mercadoPago = new MercadoPago.Builder()
                .setContext(getCurrentActivity())
                .setPublicKey(data.getString(0))
                .build();
                
                mercadoPago.getInstallments(bin, amount, issuerId, paymentMethodId, new Callback<List<Installment>>() {
                    @Override
                    public void success(List<Installment> installments) {
                        Gson gson = new Gson();
                        String installment = gson.toJson(installments);
                        callback.success(installment);
                    }
                    
                    @Override
                    public void failure(ApiException error) {
                        callback.error(error.toString());
                    }
                });
                return true;
            } else if (action.equals("getIdentificationTypes")) {
                callback = callbackContext;
                
                MercadoPago mercadoPago = new MercadoPago.Builder()
                .setContext(getCurrentActivity())
                .setPublicKey(data.getString(0))
                .build();
                
                mercadoPago.getIdentificationTypes(new Callback<List<IdentificationType>>() {
                    @Override
                    public void success(List<IdentificationType> identificationTypes) {
                        Gson gson = new Gson();
                        String identificationType = gson.toJson(identificationTypes);
                        callback.success(identificationType);
                    }
                    
                    @Override
                    public void failure(ApiException error) {
                        callback.error(error.toString());
                    }
                });
                return true;
            } else if (action.equals("createToken")) {
                callback = callbackContext;
                
                CardToken cardToken = new CardToken(data.getString(1), data.getInt(2), data.getInt(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                
                MercadoPago mercadoPago = new MercadoPago.Builder()
                .setContext(getCurrentActivity())
                .setPublicKey(data.getString(0))
                .build();
                
                mercadoPago.createToken(cardToken, new Callback<Token>() {
                    @Override
                    public void success(Token token) {
                        Gson gson = new Gson();
                        String mptoken = gson.toJson(token);
                        callback.success(mptoken);
                    }
                    
                    @Override
                    public void failure(ApiException error) {
                        callback.error(error.toString());
                    }
                });
                return true;
                
            } else if (action.equals("getBankDeals")) {
                callback = callbackContext;
                
                MercadoPago mercadoPago = new MercadoPago.Builder()
                .setContext(getCurrentActivity())
                .setPublicKey(data.getString(0))
                .build();
                
                mercadoPago.getBankDeals(new Callback<List<BankDeal>>() {
                    @Override
                    public void success(List<BankDeal> bankDeals) {
                        Gson gson = new Gson();
                        String bankDeal = gson.toJson(bankDeals);
                        callback.success(bankDeal);
                    }
                    
                    @Override
                    public void failure(ApiException error) {
                        callback.error(error.toString());
                    }
                });
                return true;
                
            } else if (action.equals("getPaymentResult")) {
                callback = callbackContext;
                
                Long paymentId = data.getLong(1);
                String paymentTypeId = data.getString(2);
                
                MercadoPago mercadoPago = new MercadoPago.Builder()
                .setContext(getCurrentActivity())
                .setPublicKey(data.getString(0))
                .build();
                
                mercadoPago.getPaymentResult(paymentId, paymentTypeId, new Callback<PaymentResult>() {
                    @Override
                    public void success(PaymentResult paymentResult) {
                        
                        Gson gson = new Gson();
                        String mpPaymentResult = gson.toJson(paymentResult);
                        callback.success(mpPaymentResult);
                    }
                    
                    @Override
                    public void failure(ApiException error) {
                        callback.error(error.toString());
                    }
                });
                return true;
                
            } else {
                return false;
            }
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if(requestCode == MercadoPago.PAYMENT_VAULT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentMethod mppaymentMethod = JsonUtil.getInstance().fromJson(data.getStringExtra("paymentMethod"), PaymentMethod.class);
                Issuer mpissuer = JsonUtil.getInstance().fromJson(data.getStringExtra("issuer"), Issuer.class);
                Token mptoken = JsonUtil.getInstance().fromJson(data.getStringExtra("token"), Token.class);
                PayerCost mppayerCost = JsonUtil.getInstance().fromJson(data.getStringExtra("payerCost"), PayerCost.class);
                Gson gson = new Gson();
                String paymentMethod = gson.toJson(mppaymentMethod);
                String issuer = gson.toJson(mpissuer);
                String token = gson.toJson(mptoken);
                String payerCost = gson.toJson(mppayerCost);
                JSONObject js = new JSONObject();
                try {
                    js.put("payment_method", paymentMethod);
                    js.put("issuer", issuer);
                    js.put("token", token);
                    js.put("payer_cost", payerCost);
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.success(js.toString());
            } else {
                if ((data != null) && (data.hasExtra("mpException"))) {
                    MPException mpException = JsonUtil.getInstance()
                    .fromJson(data.getStringExtra("mpException"), MPException.class);
                    callback.error(mpException.getMessage());
                }
            }
        }   else if (requestCode == MercadoPago.CARD_VAULT_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                PaymentMethod mppaymentMethod = JsonUtil.getInstance().fromJson(data.getStringExtra("paymentMethod"), PaymentMethod.class);
                Issuer mpissuer = JsonUtil.getInstance().fromJson(data.getStringExtra("issuer"), Issuer.class);
                Token mptoken = JsonUtil.getInstance().fromJson(data.getStringExtra("token"), Token.class);
                PayerCost mppayerCost = JsonUtil.getInstance().fromJson(data.getStringExtra("payerCost"), PayerCost.class);
                
                
                Gson gson = new Gson();
                String paymentMethod = gson.toJson(mppaymentMethod);
                String issuer = gson.toJson(mpissuer);
                String token = gson.toJson(mptoken);
                String payerCost = gson.toJson(mppayerCost);
                JSONObject js = new JSONObject();
                try {
                    js.put("payment_method", paymentMethod);
                    js.put("issuer", issuer);
                    js.put("token", token);
                    js.put("payer_cost", payerCost);
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.success(js.toString());
                
            } else {
                if ((data != null) && (data.hasExtra("mpException"))) {
                    MPException mpException = JsonUtil.getInstance()
                    .fromJson(data.getStringExtra("mpException"), MPException.class);
                    callback.error(mpException.getMessage());
                }
            }
        }else if(requestCode == MercadoPago.PAYMENT_METHODS_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                PaymentMethod paymentMethod = JsonUtil.getInstance().fromJson(data.getStringExtra("paymentMethod"), PaymentMethod.class);
                
                Gson gson = new Gson();
                callback.success(gson.toJson(paymentMethod));
            } else {
                if ((data != null) && (data.hasExtra("mpException"))) {
                    MPException mpException = JsonUtil.getInstance()
                    .fromJson(data.getStringExtra("mpException"), MPException.class);
                    callback.error(mpException.getMessage());
                }
            }
        } else if (requestCode == MercadoPago.ISSUERS_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                Issuer issuer = JsonUtil.getInstance().fromJson(data.getStringExtra("issuer"), Issuer.class);
                
                Gson gson = new Gson();
                callback.success(gson.toJson(issuer));
                
            } else {
                if ((data != null) && (data.hasExtra("mpException"))) {
                    MPException mpException = JsonUtil.getInstance()
                    .fromJson(data.getStringExtra("mpException"), MPException.class);
                    callback.error(mpException.getMessage());
                }
            }
        } else if(requestCode == MercadoPago.INSTALLMENTS_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                PayerCost payerCost = JsonUtil.getInstance().fromJson(data.getStringExtra("payerCost"), PayerCost.class);
                
                Gson gson = new Gson();
                callback.success(gson.toJson(payerCost));
            }
            else {
                if ((data != null) && (data.hasExtra("mpException"))) {
                    MPException mpException = JsonUtil.getInstance()
                    .fromJson(data.getStringExtra("mpException"), MPException.class);
                    callback.error(mpException.getMessage());
                }
            }
            
        } else if (requestCode == MercadoPago.CHECKOUT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                
                // Listo! El pago ya fue procesado por MP.
                Payment payment = JsonUtil.getInstance().fromJson(data.getStringExtra("payment"), Payment.class);
                
                if (payment != null) {
                    Gson gson = new Gson();
                    callback.success(gson.toJson(payment));
                } else {
                    callback.success("El usuario no concretó el pago.");
                }
                
            } else {
                if ((data != null) && (data.hasExtra("mpException"))) {
                    MPException mpException = JsonUtil.getInstance()
                    .fromJson(data.getStringExtra("mpException"), MPException.class);
                    callback.error(mpException.getMessage());
                }
            }
        }
        
    }
}
