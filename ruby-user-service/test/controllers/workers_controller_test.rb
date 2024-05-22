require "test_helper"

class WorkersControllerTest < ActionDispatch::IntegrationTest
  setup do
    @worker = workers(:one)
    @worker.password = "Petar#123"
    token = AuthService.login(@worker.email, @worker.password)
    @headers = { Authorization: "Bearer #{token}" }
  end

  test "should get index" do
    get api_workers_url, headers: @headers, as: :json
    assert_response :ok
  end

  test "should create worker" do
    assert_difference("Worker.count") do
      post api_workers_url, headers: @headers, params: { worker: { first_name: "Pera", last_name: "Stamenic", jmbg: "1602002710045", birth_date: 1013822400000, gender: "M", email: "pera3@gmail.rs", password: "Petar#123", username: "pera3", phone: "0631422123", address: "Jae", position: "Kmet", department: "Jae", permissions: 4294967295, active: false, firm_id: 1, daily_limit: 50000, supervisor: true } }, as: :json
    end

    assert_response :ok
  end

  test "should update worker" do
    patch api_worker_url(@worker), headers: @headers, params: { worker: { last_name: "UpdatedDoe" } }, as: :json
    assert_response :ok
  end

  test "should destroy worker" do
    assert_no_difference("Worker.count") do
      delete api_worker_url(@worker), headers: @headers, as: :json
    end

    assert_response :ok
  end
end