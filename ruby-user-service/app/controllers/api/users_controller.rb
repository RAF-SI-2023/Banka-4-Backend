class Api::UsersController < ApplicationController
  require_relative '../../utils/permission_checker'
  require_relative '../../services/auth_service'

  before_action :authenticate_user, except: %i[register]
  # before_action :wrap_params, except: %i[index destroy]

  before_action :set_user, only: %i[show update destroy ]

  # GET /api/users
  def index
    return render_unauthorized unless check_permissions?([:list_users])

    @users = User.where(active: true)
    render json: @users
  end

  # GET /api/users/1
  def show
    render json: @user
  end

  # POST /api/users/create
  def create
    return render_unauthorized unless check_permissions?([:create_users])

    @user = User.new(create_user_params)
    if @user.valid? && @user.save
      render json: @user, status: :ok
    else
      render_bad_request
    end
  end

  # POST /api/users/register
  def register
    @user = User.find_by(email: register_user_params[:email])

    return render_bad_request(error: "User not found") unless @user
    return render_unauthorized if @user.active
    return render_bad_request(error: "Verification code not given") unless params.key?(:verification_code)
    return render_bad_request(error: "Verification code not requested or doesn't match") unless (@verification_code = VerificationCode.find_by(email: register_user_params[:email], code: register_user_params[:verification_code], reset: false))

    if @user.update(register_user_params.except(:verification_code))
      @verification_code.destroy!
      render json: @user, status: :ok
    else
      render_bad_request
    end
  end

  # PATCH/PUT /api/users/id
  def update
    return render_unauthorized unless check_permissions?([:edit_users])

    if @user.update(update_user_params)
      render json: @user, status: :ok
    else
      render_bad_request
    end
  end

  # DELETE /api/users/id
  def destroy
    return render_unauthorized unless check_permissions?([:deactivate_users])

    @user.active = false
    if @user.save
      render status: :ok
    else
      render_bad_request
    end
  end

  private

  def set_user
    return if (@user = User.find(params[:id]))
    return if (@user = User.find_by(search_email_params))
    return if (@user = User.find_by(search_jmbg_params))

    @user = User.find_by(search_phone_params)
  end

  # Only allow select params when creating a user
  def create_user_params
    params.require(:user).permit(:first_name, :last_name, :jmbg, :birth_date, :gender, :email, :phone, :address, :connected_accounts, :active)
  end

  # Only allow select params when updating a user
  def update_user_params
    params.permit(:last_name, :address, :phone, :password, :connected_accounts)
  end

  # Only allow select params when registering a user
  def register_user_params
    params.permit(:email, :password, :active, :verification_code)
  end

  # Only allow select params when searching for a user by email
  def search_email_params
    params.require(:user).permit(:email)
  end

  # Only allow select params when searching for a user by JMBG
  def search_jmbg_params
    params.require(:user).permit(:jmbg)
  end

  # Only allow select params when searching for a user by phone
  def search_phone_params
    params.require(:user).permit(:phone)
  end

  def render_unauthorized
    render json: { error: 'Unauthorized' }, status: :unauthorized
  end

  def render_bad_request(error: nil)
    error_message = error || @user.errors
    render json: { error: error_message }, status: :bad_request
  end

  def authenticate_user
    authorization_header = request.headers['Authorization']
    return render_unauthorized unless authorization_header && authorization_header.start_with?('Bearer ')

    token = authorization_header.split(' ').last
    return render_unauthorized unless token

    @current_user = AuthService.authenticate_user(token)
    render_unauthorized unless @current_user
  end

  def check_permissions?(actions)
    unless @current_user && @current_user.respond_to?(:permissions)
      return false
    end

    PermissionsChecker.can_perform_actions?(@current_user.permissions, actions)
  end

  # def wrap_params
  # return if params[:user]

  # params[:user] = params.permit!.to_h
  # end
end
