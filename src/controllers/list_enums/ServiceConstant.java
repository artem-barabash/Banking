package controllers.list_enums;

import models.Service;

public class ServiceConstant {
    public static final Service education = new Service("За навчання", 5000);
    public static final Service heating = new Service("За опалення", 3000);
    public static final Service watersupply = new Service("За водопостачання", 500);
    public static final Service electrscity = new Service("За електроенергію", 300);
    public static final Service internet = new Service("За інтернет", 200);
}
