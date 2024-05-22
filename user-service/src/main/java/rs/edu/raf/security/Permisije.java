package rs.edu.raf.security;

public class Permisije {
    public static final long list_users = 0b1;
    public static final long create_users = 0b10;
    public static final long edit_users = 0b100;
    public static final long deactivate_users = 0b1000;

    public static final long list_workers = 0b10000;
    public static final long create_workers = 0b100000;
    public static final long edit_workers = 0b1000000;
    public static final long deactivate_workers = 0b10000000;

    public static final long list_firms = 0b100000000;
    public static final long create_firms = 0b1000000000;
    public static final long edit_firms = 0b10000000000;
    public static final long deactivate_firms = 0b100000000000;

    public static final long list_bank_accounts = 0b1000000000000;
    public static final long create_bank_accounts = 0b10000000000000;
    public static final long deactivate_bank_accounts = 0b100000000000000;

    public static final long list_credits = 0b1000000000000000;
    public static final long accept_credits = 0b10000000000000000;
    public static final long deny_credits = 0b100000000000000000;

    public static final long list_cards = 0b1000000000000000000;
    public static final long create_cards = 0b10000000000000000000;
    public static final long activate_cards = 0b100000000000000000000;
    public static final long deactivate_cards = 0b1000000000000000000000;
    public static final long block_cards = 0b10000000000000000000000;

    public static final long list_orders = 0b100000000000000000000000;
    public static final long accept_orders = 0b1000000000000000000000000;
    public static final long deny_orders = 0b10000000000000000000000000;

    public static final long exchange_access = 0b100000000000000000000000000;
    public static final long payment_access = 0b1000000000000000000000000000;
    public static final long action_access = 0b10000000000000000000000000000;
    public static final long option_access = 0b100000000000000000000000000000;
    public static final long order_access = 0b1000000000000000000000000000000;
    public static final long termin_access = 0b10000000000000000000000000000000;
}