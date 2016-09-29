#import "CDVPlugin.h"

@interface RNMercadopago : CDVPlugin

- (void) startCheckout:(CDVInvokedUrlCommand*)command;
- (void) getPaymentMethods:(CDVInvokedUrlCommand*)command;
- (void) setPaymentPreference:(CDVInvokedUrlCommand*)command;
- (void) getIssuers:(CDVInvokedUrlCommand*)command;
- (void) getInstallments:(CDVInvokedUrlCommand*)command;
- (void) getIdentificationTypes:(CDVInvokedUrlCommand*)command;
- (void) createToken:(CDVInvokedUrlCommand*)command;
- (void) getBankDeals:(CDVInvokedUrlCommand*)command;
- (void) getPaymentResult:(CDVInvokedUrlCommand*)command;
- (void) createPayment:(CDVInvokedUrlCommand*)command;
- (void) showPaymentVault:(CDVInvokedUrlCommand*)command;
- (void) showCardWithInstallments:(CDVInvokedUrlCommand*)command;
- (void) showCardWithoutInstallments:(CDVInvokedUrlCommand*)command;
- (void) showPaymentMethods:(CDVInvokedUrlCommand*)command;
- (void) showIssuers:(CDVInvokedUrlCommand*)command;
- (void) showInstallments:(CDVInvokedUrlCommand*)command;
- (void) showBankDeals:(CDVInvokedUrlCommand*)command;
- (void) showPaymentResult:(CDVInvokedUrlCommand*)command;
- (void) showInNavigationController:(UIViewController *)viewControllerBase;
- (NSString *) toString:(NSArray*)array;
- (NSString *) getSiteID:(NSString*)site;

@end