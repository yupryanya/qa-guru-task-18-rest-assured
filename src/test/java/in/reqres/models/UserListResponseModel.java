package in.reqres.models;

import java.util.List;

import lombok.Data;

@Data
public class UserListResponseModel {
    int page;
    int per_page;
    int total;
    int total_pages;
    List<UserData> data;
    Support support;

    @Data
    public static class UserData {
        int id;
        String email;
        String first_name;
        String last_name;
        String avatar;
    }

    @Data
    public static class Support {
        String url;
        String text;
    }
}