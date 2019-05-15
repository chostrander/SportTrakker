import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;

import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;

public class QuickStart {

    /** Application name. */
    private static final String APPLICATION_NAME =
            "Google Sheets API Java Quickstart";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    private static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                QuickStart.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

     private static Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void main(String[] args) throws IOException, ParseException {
        // Build a new authorized API client service.
        Sheets service = getSheetsService();

        // Prints the names and majors of students in a sample spreadsheet:
        // https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
        //https://docs.google.com/spreadsheets/d/10f4E_6B9NBZglLAVaSPqsz--z0jNcZ3ltCOIPwB7EEI/edit?ts=5a4ec4cd#gid=0
        String spreadsheetId = "10f4E_6B9NBZglLAVaSPqsz--z0jNcZ3ltCOIPwB7EEI";
        String range = "Schedule!A1:E";
        printRange(range,spreadsheetId,service);
        range = "Schedule!H1:L";
        printRange(range,spreadsheetId,service);
        range = "Schedule!O1:S";
        printRange(range,spreadsheetId,service);
        range = "Schedule!V1:Z";
        printRange(range,spreadsheetId,service);
        range = "Schedule!AC1:AG";
        printRange(range,spreadsheetId,service);
        range = "Schedule!AJ1:AN";
        printRange(range,spreadsheetId,service);
        range = "Schedule!AQ1:AU";
        printRange(range,spreadsheetId,service);

    }

    private static void printRange(String range, String spreadSheetId, Sheets service) throws IOException, ParseException {
        ValueRange response = service.spreadsheets().values()
                .get(spreadSheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();

         SimpleDateFormat sFormatter = new SimpleDateFormat("MMM dd, yyyy");
        if (values == null || values.size() == 0) {
            System.out.println("No data found.");
        } else {
            System.out.println("Processing Mondays");
            System.out.println("time,game,ref");
            for (List row : values) {
                if (row.size() == 1 && ((String)row.get(0)).startsWith("American Indoor")) {
                    System.out.println(row.get(0));
                } else if (row.size() == 1 && ((String)row.get(0)).endsWith("DAY")) {
                    System.out.println(row.get(0));
                } else if (row.size() == 1 && ((String)row.get(0)).matches("[a-zA-z]*\\s\\d{1,2},\\s\\d{4}")) {
                    Date date = sFormatter.parse(row.get(0).toString());

                    System.out.println("Date--> "+ date.toString());
                } else if (row.size() == 5 && !((String)row.get(0)).equalsIgnoreCase("TIMES")) {
                    System.out.printf("%s\t%s\t%s\n",row.get(0),row.get(4),row.get(2));
                } else if (row.size() == 3 && ((String)row.get(2)).contains(" v ")) {
                    System.out.printf("%s\tNo Ref\t%s\n",row.get(0),row.get(2));
                }

            }
        }
    }

}
