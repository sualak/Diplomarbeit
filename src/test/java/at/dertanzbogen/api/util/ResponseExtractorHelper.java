package at.dertanzbogen.api.util;


import io.restassured.response.Response;

public class ResponseExtractorHelper
{
    public static String extractIdFromLocation(Response response)
    {
        // Extract the location header and parse the ID from the URL
        var locationHeader = response.header("Location");
        return locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
    }
}
