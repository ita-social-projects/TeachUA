package com.softserve.teachua.config;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.google.api.services.forms.v1.Forms;
import com.google.api.services.forms.v1.FormsScopes;
import com.google.api.services.forms.v1.model.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.softserve.teachua.Application;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;

import com.softserve.teachua.model.Question;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Configuration
public class GoogleFormConfig {

    private static final String APPLICATION_NAME = "google-form-api-project";
    private static Drive driveService;

    private static Forms formsService;


    static {

        try {

            GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

            driveService = new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    jsonFactory, null)
                    .setApplicationName(APPLICATION_NAME).build();

            formsService = new Forms.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    jsonFactory, null)
                    .setApplicationName(APPLICATION_NAME).build();

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }


    private static void transformInQuiz(String formId, String token) throws IOException {
        BatchUpdateFormRequest batchRequest = new BatchUpdateFormRequest();
        Request request = new Request();
        request.setUpdateSettings(new UpdateSettingsRequest());
        request.getUpdateSettings().setSettings(new FormSettings());
        request.getUpdateSettings().getSettings().setQuizSettings(new QuizSettings());
        request.getUpdateSettings().getSettings().getQuizSettings().setIsQuiz(true);
        request.getUpdateSettings().setUpdateMask("quizSettings.isQuiz");
        batchRequest.setRequests(Collections.singletonList(request));
        formsService.forms().batchUpdate(formId, batchRequest)
                .setAccessToken(token).execute();
    }

    private static String createNewForm(String token) throws IOException {
        Form form = new Form();
        form.setInfo(new Info());
        form.getInfo().setTitle("New Form Quiz Created from Java");
        form = formsService.forms().create(form)
                .setAccessToken(token)
                .execute();
        return form.getFormId();
    }

    public static boolean publishForm(String formId, String token) throws GeneralSecurityException, IOException {

        PermissionList list = driveService.permissions().list(formId).setOauthToken(token).execute();

        if (list.getPermissions().stream().filter((it) -> it.getRole().equals("reader")).findAny().equals(null)) {
            Permission body = new Permission();
            body.setRole("reader");
            body.setType("anyone");
            driveService.permissions().create(formId, body).setOauthToken(token).execute();
            return true;
        }

        return false;
    }

    private static ListFormResponsesResponse readResponses(String formId, String token) throws IOException {
        return formsService.forms().responses().list(formId).setOauthToken(token).execute();
    }



    public static Form readFormInfo(String formId, String token) throws IOException {
        return formsService.forms().get(formId).setOauthToken(token).execute();
    }

    public static String getAccessToken() throws IOException {
        GoogleCredentials credential = GoogleCredentials.fromStream(Objects.requireNonNull(
                Application.class.getResourceAsStream("/stoked-name-368217-5a1b3a941e8c.json"))).createScoped(FormsScopes.all());
        return credential.getAccessToken() != null ?
                credential.getAccessToken().getTokenValue() :
                credential.refreshAccessToken().getTokenValue();
    }

    public static void changeDescription(String formId, String token,
                                         String description,
                                         String title) throws IOException {
        BatchUpdateFormRequest batchRequest = new BatchUpdateFormRequest();
        Request request = new Request();
        request.setUpdateFormInfo(new UpdateFormInfoRequest());
        request.getUpdateFormInfo().setInfo(new Info());
        request.getUpdateFormInfo().getInfo().setDescription(description)
                .setTitle(title);
        request.getUpdateFormInfo().setUpdateMask("description, title");
        batchRequest.setRequests(Collections.singletonList(request));

        formsService.forms().batchUpdate(formId, batchRequest)
                .setAccessToken(token).execute();
    }

    public static Map<Integer, Item> getItemIndex(String formId, String itemId, String token) throws IOException {
        Form form = readFormInfo(formId, token);
        Item newItem = new Item();
        List<Item> items = form.getItems();
        for (Item item : items) {
            if (item.getItemId().equals(itemId)) {
                newItem = item;
            }
        }
        int itemIndex = 0;
        for (Item item : items) {
            if (item.getItemId().equals(newItem.getItemId())) {
                itemIndex = (items.indexOf(newItem));
            }

        }
        Map<Integer, Item> itemMap = new HashMap<>();
        itemMap.put(itemIndex, newItem);
        return itemMap;
    }


}

