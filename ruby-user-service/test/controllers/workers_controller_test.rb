require "test_helper"

class WorkersControllerTest < ActionDispatch::IntegrationTest
  setup do
    @worker = workers(:one)
  end

  test "should get index" do
    get workers_url, as: :json
    assert_response :success
  end

  test "should create worker" do
    assert_difference("Worker.count") do
      post workers_url, params: { worker: { active: @worker.active, address: @worker.address, approval_flag: @worker.approval_flag, birth_date: @worker.birth_date, daily_limit: @worker.daily_limit, daily_spent: @worker.daily_spent, department: @worker.department, email: @worker.email, firmId_id: @worker.firmId_id, first_name: @worker.first_name, gender: @worker.gender, jmbg: @worker.jmbg, last_name: @worker.last_name, password: "secret", password_confirmation: "secret", permission: @worker.permission, phone: @worker.phone, position: @worker.position, string: @worker.string, supervisor: @worker.supervisor, username: @worker.username } }, as: :json
    end

    assert_response :created
  end

  test "should show worker" do
    get worker_url(@worker), as: :json
    assert_response :success
  end

  test "should update worker" do
    patch worker_url(@worker), params: { worker: { active: @worker.active, address: @worker.address, approval_flag: @worker.approval_flag, birth_date: @worker.birth_date, daily_limit: @worker.daily_limit, daily_spent: @worker.daily_spent, department: @worker.department, email: @worker.email, firmId_id: @worker.firmId_id, first_name: @worker.first_name, gender: @worker.gender, jmbg: @worker.jmbg, last_name: @worker.last_name, password: "secret", password_confirmation: "secret", permission: @worker.permission, phone: @worker.phone, position: @worker.position, string: @worker.string, supervisor: @worker.supervisor, username: @worker.username } }, as: :json
    assert_response :success
  end

  test "should destroy worker" do
    assert_difference("Worker.count", -1) do
      delete worker_url(@worker), as: :json
    end

    assert_response :no_content
  end
end
