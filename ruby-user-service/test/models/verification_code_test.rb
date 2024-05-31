require "test_helper"

class VerificationCodeTest < ActiveSupport::TestCase
  def setup
    @verification_code = verification_codes(:one)
  end

  test "should be valid with valid attributes" do
    assert @verification_code.valid?
  end

  test "email should be present" do
    @verification_code.email = nil
    assert_not @verification_code.valid?
    assert_includes @verification_code.errors[:email], "email can't be blank"
  end

  test "email should be valid format" do
    @verification_code.email = 'invalid-email'
    assert_not @verification_code.valid?
    assert_includes @verification_code.errors[:email], "is invalid"
  end

  test "code should be present" do
    @verification_code.code = nil
    assert_not @verification_code.valid?
    assert_includes @verification_code.errors[:code], "code can't be blank"
  end

  test "expiration should be present" do
    @verification_code.expiration = nil
    assert_not @verification_code.valid?
    assert_includes @verification_code.errors[:expiration], "expiration can't be blank"
  end

  test "expiration should be in the future" do
    @verification_code.expiration = Time.now.to_i * 1000 - 1000
    assert_not @verification_code.valid?
    assert_includes @verification_code.errors[:expiration], "expiration must be in the future"
  end

  test "reset should be present" do
    @verification_code.reset = nil
    assert_not @verification_code.valid?
    assert_includes @verification_code.errors[:reset], "reset can't be blank"
  end
end