package com.example.sendmail;

import java.io.File;


public class SendMailUtil {

//	private static final String HOST = "smtp.qq.com";
//	private static final String PORT = "587";
//	private static final String FROM_ADD = "784803793@qq.com";
//	private static final String FROM_PSW = "8737691qb^-^";

    public static String HOST = "smtp.163.com";
    public static String PORT = "25";
    public static String FROM_ADD = "workserver_qin@163.com";
    public static String AUTH_PWD = "q15070854543b";

    public static void send(String toAddr, String subject, String content, final File file) {
        final MailInfo mailInfo = createMail(toAddr, subject, content);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.sendFileMail(mailInfo, file);
            }
        }).start();
    }

    public static void send(String toAddr, String subject, String content) {
        final MailInfo mailInfo = createMail(toAddr, subject, content);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {

            @Override
            public void run() {
                sms.sendTextMail(mailInfo);
            }
        }).start();
    }

    private static MailInfo createMail(String toAddr, String subject, String content) {
        final MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost(HOST);
        mailInfo.setMailServerPort(PORT);
        mailInfo.setValidate(true);
        mailInfo.setUserName(FROM_ADD);
        mailInfo.setPassword(AUTH_PWD);
        mailInfo.setFromAddress(FROM_ADD);
        mailInfo.setToAddress(toAddr);
        mailInfo.setSubject(subject);
        mailInfo.setContent(content);
        return mailInfo;
    }
}
