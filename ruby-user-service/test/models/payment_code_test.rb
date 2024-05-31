require "test_helper"

class PaymentCodeTest < ActiveSupport::TestCase
  def setup
    @payment_code = payment_codes(:one)
  end

  test "should be valid with valid attributes" do
    assert @payment_code.valid?
  end

  test "form_and_basis should be present" do
    @payment_code.form_and_basis = nil
    assert_not @payment_code.valid?
    assert_includes @payment_code.errors[:form_and_basis], "form and basis can't be blank"
  end

  test "form_and_basis should be an integer" do
    @payment_code.form_and_basis = 'abc'
    assert_not @payment_code.valid?
    assert_includes @payment_code.errors[:form_and_basis], "must be between 100 and 999"
  end

  test "form_and_basis should be greater than or equal to 100" do
    @payment_code.form_and_basis = 99
    assert_not @payment_code.valid?
    assert_includes @payment_code.errors[:form_and_basis], "must be between 100 and 999"
  end

  test "form_and_basis should be less than 1000" do
    @payment_code.form_and_basis = 1000
    assert_not @payment_code.valid?
    assert_includes @payment_code.errors[:form_and_basis], "must be between 100 and 999"
  end

  test "payment_description should be present" do
    @payment_code.payment_description = nil
    assert_not @payment_code.valid?
    assert_includes @payment_code.errors[:payment_description], "payment description can't be blank"
  end
end