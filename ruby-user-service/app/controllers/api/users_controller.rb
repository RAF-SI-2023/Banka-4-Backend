class Api::UsersController < ApplicationController
  before_action :set_user, only: %i[update destroy]
  before_action :authenticate_user, except: [:register]

  def index
    @users = User.all
    render json: @users
  end

  def create
    actions = [:create_users]
    render_unauthorized unless PermissionsChecker.can_perform_actions?(@current_user.permissions, actions)

    @user = User.new(create_user_params)
    if @user.valid? && @user.save
      render json: @user, status: :ok
    else
      render_bad_request
    end
  end

  def register
    @user = User.find_by(email: register_user_params[:email])
    if @user && @user.active
      render_unauthorized
    elsif @user && @user.update(register_user_params)
      render json: @user, status: :ok
    else
      render_bad_request
    end
  end

  def update
    actions = [:edit_users]
    render_unauthorized unless PermissionsChecker.can_perform_actions?(@current_user.permissions, actions)

    if @user.update(update_user_params)
      render json: @user, status: :ok
    else
      render_bad_request
    end
  end

  def destroy
    actions = [:deactivate_users]
    render_unauthorized unless PermissionsChecker.can_perform_actions?(@current_user.permissions, actions)

    @user.active = false
    if @user.save
      render status: :ok
    else
      render_bad_request
    end
  end

  private

  def set_user
    @user = User.find(params[:id])
  end

  def create_user_params
    params.require(:user).permit(:first_name, :last_name, :jmbg, :birth_date, :gender, :email, :phone, :address, :connected_accounts, :active)
  end

  def update_user_params
    params.require(:user).permit(:last_name, :address, :phone, :password, :connected_accounts, :active)
  end

  def register_user_params
    params.permit(:email, :password, :active)
  end

  def user_params
    params.require(:user).permit(:first_name, :last_name, :jmbg, :birth_date, :gender, :email, :password, :password_confirmation, :phone, :address, :connected_accounts, :active)
  end

  def authenticate_user
    token = request.headers['Authorization']&.split(' ')&.last
    return render_unauthorized unless token

    payload = decode_jwt(token)
    return render_unauthorized unless payload

    @current_user = User.find_by(id: payload['id'])
    render_unauthorized unless @current_user
  end

  def decode_jwt(token)
    JWT.decode(token, AuthServiceImpl::JWT_SECRET_KEY, true, algorithm: 'HS256').first
  rescue JWT::DecodeError
    nil
  end

  def render_unauthorized
    render json: { error: 'Unauthorized' }, status: :unauthorized
  end

  def render_bad_request
    render json: { error: 'Bad Request' }, status: :bad_request
  end
end
