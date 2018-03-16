/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enlteledger;

import java.awt.GraphicsEnvironment;
import java.io.Console;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author new
 */
public class EnlteWallet {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Recipient's email ID needs to be mentioned.
        /* String to = "dono4fire@gmail.com";

      // Sender's email ID needs to be mentioned
      String from = "enlteappdev@gmail.com";

      // Assuming you are sending email from localhost
      String host = " smtp.1and1.com smtp.1and1.com";//http://82.223.43.148/

      // Get system properties
      Properties properties = System.getProperties();

      // Setup mail server
      properties.setProperty("mail.smtp.host", host);

      // Get the default Session object.
      Session session = Session.getDefaultInstance(properties);

      try {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

         // Set Subject: header field
         message.setSubject("This is the Subject Line!");

         // Now set the actual message
         message.setText("This is actual message");

         // Send message
         Transport.send(message);
         System.out.println("Sent message successfully....");
      } catch (MessagingException mex) {
         mex.printStackTrace();
      }*/
 /*  final String username = "username@gmail.com";
        final String password = "password";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("enlteappdev@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("dono4fire@gmail.com"));
            message.setSubject("Testing Subject");
            message.setText("Dear Mail Crawler,"
                    + "\n\n No spam to my email, please!");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

         */
        try {
            //It opens Command prompt.
            openConsole();
        } catch (IOException ex) {
            Logger.getLogger(EnlteWallet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(EnlteWallet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(EnlteWallet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void openConsole() throws IOException, InterruptedException, URISyntaxException {
        Console console = System.console();
        if (console == null && !GraphicsEnvironment.isHeadless()) {
            //Sample.class.getProtectionDomain().getCodeSource().getLocation().toString()
            String filename = EnlteWallet.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            filename = filename.replace("build/classes/", "");
//            if(!filename.contains("dist/EnlteLedger.jar")){
//                 filename = filename + "dist/EnlteLedger.jar";
//            }           
            System.out.println("sample.Sample.main()" + filename);
            // Command to run .jar file in Command prompt.
            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", "cmd", "/k", "java -jar \"" + filename + "\""});
        } else {
            //Sample.main(new String[0]);
            //Timer time = new Timer(); // Instantiate Timer Object
            //ScheduledTask st = new ScheduledTask(); // Instantiate SheduledTask class
            //time.schedule(st, 0, 3000); // Create Repetitively task for every 1 secs
            // TODO code application logic here
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter your wallet id");
            String walletId = scanner.next();
            hitBlockChain(walletId);
        } //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * It get latest hash from api and update on console
     */
    private static void hitBlockChain(String walletId) {
        try {
            System.out.println("Loading...");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost("http://enlte.com/index/wallet_info");
            //37fa3882f58a932182a1c28150c5f17c25d1d2dd0fd00050cbc1c291bb23cfff
            JSONObject json = new JSONObject();
            json.put("wallet_id", walletId);

            List<NameValuePair> params = new ArrayList<NameValuePair>(1);
            params.add(new BasicNameValuePair("wallet_id", walletId));
            postRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
//            StringEntity input = new StringEntity(json.toString());
//            input.setContentType("application/json");
//            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            String result = EntityUtils.toString(response.getEntity());
            System.out.println("  ");
            handleResult(result, walletId);

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EnlteWallet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EnlteWallet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(EnlteWallet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void handleResult(String res, String walletId) {

        JSONObject jo = null;
        try {
            jo = new JSONObject(res);
            if (jo.has("status")) {
                try {
                    String status = jo.getString("status");
                    if (status.equals("true")) {
                        if (jo.has("data")) {
                            try {
                                JSONArray result = jo.getJSONArray("data");
                                if (result != null) {
                                    for (int i = 0; i < result.length(); i++) {
                                        JSONObject jsonObject = result.getJSONObject(i);
                                        String balance = "0";
                                        if (jsonObject.has("Balance")) {
                                            balance = jsonObject.getString("Balance");
                                        }
                                        String transactions = "0";
                                        if (jsonObject.has("Transactions")) {
                                            transactions = jsonObject.getString("Transactions");
                                        }
                                        System.out.println("Balance: " + balance + " Coins");
                                        System.out.println("Transactions: " + transactions);
                                        //callback.onWalletInfoSuccess(balance, transactions);
                                        System.out.println("  ");
                                    }
                                }
                                //hash.setVisibility(View.VISIBLE);

                            } catch (Exception e) {
                                //callback.onWalletInfoSuccess("Error", e.getMessage());
                                System.out.println("Exception:-" + e);
                            }
                        }
                        //Toast.makeText(mContext, "You are now following " + hash, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("JSONException:-" + e);
                }

            } else {
                System.out.println("Server error.");
                //Toast.makeText(mContext, "Server error.", Toast.LENGTH_LONG).show();
                //resultCallback.onError(res);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("JSONException:-" + e);
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter any character to refresh wallet or enter other user's wallet id");
        String userId = scanner.next();
        if (userId != null && userId.length() > 0) {
            if (userId.length() == 64) {
                walletId = userId;
                hitBlockChain(walletId);
            } else if (userId.contains("exit")) {
                System.exit(1);
            } else {
                hitBlockChain(walletId);
            }

        }
    }

}
