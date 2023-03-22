package com.example.prodajem_kupujem.config;

import java.util.List;

public class Constants {

    public static  final String SECRET="2948404D635166546A576E5A7134743777217A25432A462D4A614E645267556B";

    public static  final int TOKEN_EXPIRATION=90000000;

    public static  final int REFRESH_TOKEN_EXPIRATION=180000000;


    public static  final String ADVERTISEMENT_STATUS_ACTIVE="ACTIVE";
    public static  final int ADVERTISEMENT_STATUS_ACTIVE_=1;


    public static  final String ADVERTISEMENT_STATUS_EXPIRES="EXPIRES";

    public static  final String ADVERTISEMENT_STATUS_EXPIRED="EXPIRED";

    public static  final String ADVERTISEMENT_SORT_DATE_NEWEST="creationDate,desc";
    public static  final String ADVERTISEMENT_SORT_PROMOTION="advertisementPromotion,desc";

    public static  final String ADVERTISEMENT_SORT_DATE_OLDEST="creationDate,asc";
    public static  final String ADVERTISEMENT_SORT_CHEAPER="price,asc";
    public static  final String ADVERTISEMENT_SORT_EXPENSIVE="price,desc";

    public static  final int ADVERTISEMENT_PROMOTION_RESTORE=2;

    public static  final int ADVERTISEMENT_PROMOTION_STANDARD=1;
    public static  final int PAGE_SIZE=5;

    public static  final int VALIDATION_TOKEN_EXPIRATION=2;

    public static  final String EMAIL_SENDER="aleksastarcevic1@gmail.com";
    public static  final String EMAIL_CONFIRMATION_TEMPLATE ="MailConfirmationTemplate.html";

    public static final List<String> AVAILABLE_SORTS = List.of(ADVERTISEMENT_SORT_DATE_NEWEST,ADVERTISEMENT_SORT_DATE_OLDEST,ADVERTISEMENT_SORT_PROMOTION,ADVERTISEMENT_SORT_CHEAPER,ADVERTISEMENT_SORT_EXPENSIVE);
    public static final List<String> AVAILABLE_STATUS = List.of(ADVERTISEMENT_STATUS_ACTIVE,ADVERTISEMENT_STATUS_EXPIRED,ADVERTISEMENT_STATUS_EXPIRES);




}
