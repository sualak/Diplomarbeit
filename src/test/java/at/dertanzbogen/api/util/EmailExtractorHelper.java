package at.dertanzbogen.api.util;


import at.dertanzbogen.api.email.EmailService;
import at.dertanzbogen.api.email.LoggerMailSenderImpl;
//import at.dertanzbogen.api.security.verificationToken.VerificationToken;
import at.dertanzbogen.api.security_token_auth.verificationToken.VerificationToken;

public class EmailExtractorHelper
{
    public static String extractTokenId(LoggerMailSenderImpl mailSender)
    {
        var body = mailSender.getEmailDTO().body();

        // ..tokenId=xxx..
        var tokenIdIndex = body.indexOf(EmailService.TOKEN_PARAM);

        // Extract the token id from the email body (e.g. "xxx")
        var startIndex = tokenIdIndex + EmailService.TOKEN_PARAM.length() + 1;
        var endIndex = startIndex + VerificationToken.VALIDATOR_CLEAR_LENGTH;
        return body.substring(startIndex, endIndex);
    }
}
