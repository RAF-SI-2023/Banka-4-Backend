require "test_helper"

class WorkerTest < ActiveSupport::TestCase
  def setup
    @worker = workers(:two)
    @worker.password = "Petar#123"
  end

  test "should be valid with valid attributes" do
    assert @worker.valid?
  end

  test "first name should be present" do
    @worker.first_name = nil
    assert_not @worker.valid?
    assert_includes @worker.errors[:first_name], "first name can't be blank"
  end

  test "first name should only allow letters" do
    @worker.first_name = 'P3ra'
    assert_not @worker.valid?
    assert_includes @worker.errors[:first_name], "first name only allows letters"
  end

  test "last name should be present" do
    @worker.last_name = nil
    assert_not @worker.valid?
    assert_includes @worker.errors[:last_name], "last name can't be blank"
  end

  test "last name should only allow letters" do
    @worker.last_name = 'St@menic'
    assert_not @worker.valid?
    assert_includes @worker.errors[:last_name], "last name only allows letters"
  end

  test "username should be present" do
    @worker.username = nil
    assert_not @worker.valid?
    assert_includes @worker.errors[:username], "username can't be blank"
  end

  test "username should be unique" do
    duplicate_worker = @worker.dup
    @worker.save
    assert_not duplicate_worker.valid?
    assert_includes duplicate_worker.errors[:username], "username has to be unique"
  end

  test "birth date should be present" do
    @worker.birth_date = nil
    assert_not @worker.valid?
    assert_includes @worker.errors[:birth_date], "birth date can't be blank"
  end

  test "jmbg should be present" do
    @worker.jmbg = nil
    assert_not @worker.valid?
    assert_includes @worker.errors[:jmbg], "jmbg can't be blank"
  end

  test "jmbg should be unique" do
    duplicate_worker = @worker.dup
    @worker.save
    assert_not duplicate_worker.valid?
    assert_includes duplicate_worker.errors[:jmbg], "jmbg has to be unique"
  end

  test "jmbg should be numeric" do
    @worker.jmbg = 'abc'
    assert_not @worker.valid?
    assert_includes @worker.errors[:jmbg], "jmbg only allows numbers"
  end

  test "jmbg should be 13 characters long" do
    @worker.jmbg = '123456789'
    assert_not @worker.valid?
    assert_includes @worker.errors[:jmbg], "is the wrong length (should be 13 characters)"
  end

  test "jmbg last 3 digits should be valid for gender" do
    @worker.gender = 'M'
    @worker.jmbg = '1602002710555'
    assert_not @worker.valid?
    assert_includes @worker.errors[:jmbg], "male jmbg ends with last 3 digits below 500"

    @worker.gender = 'F'
    @worker.jmbg = '1602002710000'
    assert_not @worker.valid?
    assert_includes @worker.errors[:jmbg], "female jmbg ends with last 3 digits above 499"
  end

  test "jmbg date should match birth date" do
    @worker.jmbg = '0101992710045'
    @worker.birth_date = Time.new(2000, 1, 1).to_i * 1000
    assert_not @worker.valid?
    assert_includes @worker.errors[:jmbg], "JMBG and date do not match"
  end

  test "gender should be present" do
    @worker.gender = nil
    assert_not @worker.valid?
    assert_includes @worker.errors[:gender], "gender can't be blank"
  end

  test "gender should only allow M or F" do
    @worker.gender = 'X'
    assert_not @worker.valid?
    assert_includes @worker.errors[:gender], "only allows M/F"
  end

  test "email should be present" do
    @worker.email = nil
    assert_not @worker.valid?
    assert_includes @worker.errors[:email], "email can't be blank"
  end

  test "email should be unique" do
    duplicate_worker = @worker.dup
    @worker.save
    assert_not duplicate_worker.valid?
    assert_includes duplicate_worker.errors[:email], "email has to be unique"
  end

  test "email should be valid format" do
    @worker.email = 'invalid-email'
    assert_not @worker.valid?
    assert_includes @worker.errors[:email], "is invalid"
  end

  test "phone should be present" do
    @worker.phone = nil
    assert_not @worker.valid?
    assert_includes @worker.errors[:phone], "phone can't be blank"
  end

  test "phone should be unique" do
    duplicate_worker = @worker.dup
    @worker.save
    assert_not duplicate_worker.valid?
    assert_includes duplicate_worker.errors[:phone], "phone has to be unique"
  end

  test "phone should be valid format" do
    @worker.phone = 'invalid-phone'
    assert_not @worker.valid?
    assert_includes @worker.errors[:phone], "phone number has to be numbers with an optional + in the start"
  end

  test "address should be present" do
    @worker.address = nil
    assert_not @worker.valid?
    assert_includes @worker.errors[:address], "address can't be blank"
  end

  test "password should be present" do
    @worker.password = nil
    assert_not @worker.valid?
    assert_includes @worker.errors[:password], "password can't be blank"
  end

  test "password should have at least 8 characters" do
    @worker.password = 'short1!'
    assert_not @worker.valid?
    assert_includes @worker.errors[:password], "password has to have between 8 and 32 characters"
  end

  test "password should have at most 32 characters" do
    @worker.password = 'a' * 33
    assert_not @worker.valid?
    assert_includes @worker.errors[:password], "password has to have between 8 and 32 characters"
  end

  test "password should contain at least one lowercase letter" do
    @worker.password = 'PASSWORD1!'
    assert_not @worker.valid?
    assert_includes @worker.errors[:password], "password has to contain at least 1 small letter"
  end

  test "password should contain at least one uppercase letter" do
    @worker.password = 'password1!'
    assert_not @worker.valid?
    assert_includes @worker.errors[:password], "password has to contain at least 1 big letter"
  end

  test "password should contain at least one digit" do
    @worker.password = 'Password!'
    assert_not @worker.valid?
    assert_includes @worker.errors[:password], "password has to contain at least 1 number"
  end

  test "password should contain at least one special character" do
    @worker.password = 'Password1'
    assert_not @worker.valid?
    assert_includes @worker.errors[:password], "password has to contain at least 1 special character"
  end

  test "position should be present" do
    @worker.position = nil
    assert_not @worker.valid?
    assert_includes @worker.errors[:position], "position can't be blank"
  end

  test "position should only allow letters" do
    @worker.position = 'Kmet1'
    assert_not @worker.valid?
    assert_includes @worker.errors[:position], "last name only allows letters"
  end

  test "department should be present" do
    @worker.department = nil
    assert_not @worker.valid?
    assert_includes @worker.errors[:department], "department can't be blank"
  end

  test "department should only allow letters" do
    @worker.department = 'Jae1'
    assert_not @worker.valid?
    assert_includes @worker.errors[:department], "last name only allows letters"
  end

  test "firm_id should be present" do
    @worker.firm_id = nil
    assert_not @worker.valid?
    assert_includes @worker.errors[:firm_id], "firm_id can't be blank"
  end

  test "firm_id should be numeric" do
    @worker.firm_id = 'abc'
    assert_not @worker.valid?
    assert_includes @worker.errors[:firm_id], "firm_id only allows numbers"
  end

  test "daily limit should be present" do
    @worker.daily_limit = nil
    assert_not @worker.valid?
    assert_includes @worker.errors[:daily_limit], "daily_limit can't be blank"
  end

  test "daily limit should be greater than or equal to 0" do
    @worker.daily_limit = -1
    assert_not @worker.valid?
    assert_includes @worker.errors[:daily_limit], "daily_limit must be greater than 0"
  end

  test "daily spent should be greater than or equal to 0 if present" do
    @worker.daily_spent = -1
    assert_not @worker.valid?
    assert_includes @worker.errors[:daily_spent], "daily_spent must be greater than 0"
  end
end