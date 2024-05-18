require "test_helper"

class UserTest < ActiveSupport::TestCase
  def setup
    @valid_user = User.new(
      first_name: "Test",
      last_name: "Testovic",
      email: "testiranje@raf.rs",
      password: "Aleksa#123",
      password_digest: "$2a$12$Bghy.aJ.VDBLVrkbPhodxO6mOBXV5mQpfmGQ9oEHwpKXofcuIWhIW",
      jmbg: "1602002000001",
      birth_date: 1013822400000,
      gender: "M",
      phone: "0",
      address: "Egg",
      connected_accounts: "444000000000000333",
      active: true
    )
  end

  test "should be valid with all attributes" do
    assert @valid_user.valid?
  end

  test "should not be valid without first name" do
    @valid_user.first_name = nil
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:first_name], "first name can't be blank"
  end

  test "should not be valid with non-alphabetic first name" do
    @valid_user.first_name = 'Aleksa123'
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:first_name], "first name only allows letters"
  end

  test "should not be valid without last name" do
    @valid_user.last_name = nil
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:last_name], "last name can't be blank"
  end

  test "should not be valid with non-alphabetic last name" do
    @valid_user.last_name = 'Buncic123'
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:last_name], "last name only allows letters"
  end

  test "should not be valid without birth date" do
    @valid_user.birth_date = nil
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:birth_date], "birth date can't be blank"
  end

  test "should not be valid without jmbg" do
    @valid_user.jmbg = nil
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:jmbg], "jmbg can't be blank"
  end

  test "should not be valid with non-unique jmbg" do
    duplicate_user = @valid_user.dup
    @valid_user.save
    assert_not duplicate_user.valid?
    assert_includes duplicate_user.errors[:jmbg], "jmbg has to be unique"
  end

  test "should not be valid with incorrect length jmbg" do
    @valid_user.jmbg = '123456789'
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:jmbg], "is the wrong length (should be 13 characters)"
  end

  test "should not be valid with non-numeric jmbg" do
    @valid_user.jmbg = 'a' * 13
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:jmbg], "jmbg only allows numbers"
  end

  test "should not be valid if gender is M and jmbg ends with digits above 499" do
    @valid_user.jmbg = '1602002710500'
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:jmbg], "male jmbg ends with last 3 digits below 500"
  end

  test "should not be valid if gender is F and jmbg ends with digits below 500" do
    @valid_user.gender = 'F'
    @valid_user.jmbg = '1602002710499'
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:jmbg], "female jmbg ends with last 3 digits above 499"
  end

  test "should not be valid if jmbg does not match birth date" do
    @valid_user.jmbg = '0101002710009'
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:jmbg], "JMBG and date do not match"
  end

  test "should not be valid without gender" do
    @valid_user.gender = nil
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:gender], "gender can't be blank"
  end

  test "should not be valid with incorrect gender value" do
    @valid_user.gender = 'X'
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:gender], "only allows M/F"
  end

  test "should not be valid without email" do
    @valid_user.email = nil
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:email], "email can't be blank"
  end

  test "should not be valid with incorrect email format" do
    @valid_user.email = 'invalid_email'
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:email], "is invalid"
  end

  test "should not be valid without phone" do
    @valid_user.phone = nil
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:phone], "phone can't be blank"
  end

  test "should not be valid with non-numeric phone" do
    @valid_user.phone = 'abc'
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:phone], "phone number has to be numbers with an optional + in the start"
  end

  test "should not be valid without address" do
    @valid_user.address = nil
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:address], "address can't be blank"
  end

  test "should not be valid without connected accounts" do
    @valid_user.connected_accounts = nil
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:connected_accounts], "connected accounts can't be blank"
  end

  test "should not be valid without active status" do
    @valid_user.active = nil
    assert_not @valid_user.valid?
    assert_includes @valid_user.errors[:active], "active can't be blank"
  end

  test "should validate password on update if present" do
    @valid_user.password = "short"
    assert_not @valid_user.valid?(:update)
    assert_includes @valid_user.errors[:password], "password has to have between 8 and 32 characters"
  end

  test "should validate password presence on update" do
    @valid_user.password = nil
    assert_not @valid_user.valid?(:update)
    assert_includes @valid_user.errors[:password], "password can't be blank"
  end

  test "should validate password complexity on update" do
    @valid_user.password =  "Aleksa123"
    assert_not @valid_user.valid?(:update)
    assert_includes @valid_user.errors[:password], "password has to contain at least 1 special character"
  end

  test "should encrypt password before update if present" do
    @valid_user.update(password: 'Aleksa#123')
    assert_not_nil @valid_user.password_digest
  end
end