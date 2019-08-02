import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRole, defaultValue } from 'app/shared/model/role.model';

export const ACTION_TYPES = {
  FETCH_ROLE_LIST: 'role/FETCH_ROLE_LIST',
  FETCH_ROLE: 'role/FETCH_ROLE',
  CREATE_ROLE: 'role/CREATE_ROLE',
  UPDATE_ROLE: 'role/UPDATE_ROLE',
  DELETE_ROLE: 'role/DELETE_ROLE',
  RESET: 'role/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRole>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type RoleState = Readonly<typeof initialState>;

// Reducer

export default (state: RoleState = initialState, action): RoleState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ROLE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ROLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ROLE):
    case REQUEST(ACTION_TYPES.UPDATE_ROLE):
    case REQUEST(ACTION_TYPES.DELETE_ROLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ROLE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ROLE):
    case FAILURE(ACTION_TYPES.CREATE_ROLE):
    case FAILURE(ACTION_TYPES.UPDATE_ROLE):
    case FAILURE(ACTION_TYPES.DELETE_ROLE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ROLE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ROLE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ROLE):
    case SUCCESS(ACTION_TYPES.UPDATE_ROLE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ROLE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/roles';

// Actions

export const getEntities: ICrudGetAllAction<IRole> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ROLE_LIST,
    payload: axios.get<IRole>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IRole> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ROLE,
    payload: axios.get<IRole>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRole> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ROLE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRole> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ROLE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRole> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ROLE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
