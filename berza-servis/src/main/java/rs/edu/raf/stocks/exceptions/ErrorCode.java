package rs.edu.raf.stocks.exceptions;

public enum ErrorCode {
    STOCK_ALREADY_EXISTS,
    API_LIMIT_REACHED,
    TICKER_DOESNT_EXIST,
    BAD_DATE_INPUT,

    USER_DOESNT_OWN_TICKER,

    USER_DOESNT_OWN_QUANTITY_TICKER,

    OTC_OFFER_DOESNT_EXIST,

    AMOUNT_OF_OTC_DOESNT_EXIST,

    PRICE_MUST_BE_POSITIVE,

    YOU_DONT_OWN_THIS_OTC,

    OTC_OFFER_CURRENTLY_PLACED
}
