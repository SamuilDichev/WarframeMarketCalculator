package models;

/**
 * Created by Samuil on 26.8.2017 г..
 */
public class ApiResult {
  private int code;
  private Response response;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public Response getResponse() {
    return response;
  }

  public void setResponse(Response response) {
    this.response = response;
  }
}
