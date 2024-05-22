# frozen_string_literal: true

class PermissionsChecker
  PERMISSION_MAP = {
    list_users: 0b1,
    create_users: 0b10,
    edit_users: 0b100,
    deactivate_users: 0b1000,

    list_workers: 0b10000,
    create_workers: 0b100000,
    edit_workers: 0b1000000,
    deactivate_workers: 0b10000000,

    list_firms: 0b100000000,
    create_firms: 0b1000000000,
    edit_firms: 0b10000000000,
    deactivate_firms: 0b100000000000,

    list_bank_accounts: 0b1000000000000,
    create_bank_accounts: 0b10000000000000,
    deactivate_bank_accounts: 0b100000000000000,

    list_credits: 0b1000000000000000,
    accept_credits: 0b10000000000000000,
    deny_credits: 0b100000000000000000,

    list_cards: 0b1000000000000000000,
    activate_cards: 0b10000000000000000000,
    deactivate_cards: 0b100000000000000000000,
    block_cards: 0b1000000000000000000000,

    list_orders: 0b10000000000000000000000,
    accept_orders: 0b100000000000000000000000,
    deny_orders: 0b1000000000000000000000000,

    exchange_access: 0b10000000000000000000000000,
    payment_access: 0b100000000000000000000000000,
    action_access: 0b1000000000000000000000000000,
    option_access: 0b10000000000000000000000000000,
    order_access: 0b100000000000000000000000000000,
    termin_access: 0b100000000000000000000000000
  }.freeze

  def self.can_perform_actions?(user_permissions, actions)
    required_permissions = actions.map { |action| PERMISSION_MAP[action.to_sym] }
    user_permissions_bits = PERMISSION_MAP.values.select { |perm| (user_permissions & perm) == perm }

    (required_permissions - user_permissions_bits).empty?
  end
end