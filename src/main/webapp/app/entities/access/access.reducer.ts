import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAccess, defaultValue } from 'app/shared/model/access.model';

export const ACTION_TYPES = {
  FETCH_ACCESS_LIST: 'access/FETCH_ACCESS_LIST',
  FETCH_ACCESS: 'access/FETCH_ACCESS',
  CREATE_ACCESS: 'access/CREATE_ACCESS',
  UPDATE_ACCESS: 'access/UPDATE_ACCESS',
  DELETE_ACCESS: 'access/DELETE_ACCESS',
  RESET: 'access/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAccess>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type AccessState = Readonly<typeof initialState>;

// Reducer

export default (state: AccessState = initialState, action): AccessState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ACCESS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ACCESS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ACCESS):
    case REQUEST(ACTION_TYPES.UPDATE_ACCESS):
    case REQUEST(ACTION_TYPES.DELETE_ACCESS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ACCESS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ACCESS):
    case FAILURE(ACTION_TYPES.CREATE_ACCESS):
    case FAILURE(ACTION_TYPES.UPDATE_ACCESS):
    case FAILURE(ACTION_TYPES.DELETE_ACCESS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACCESS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACCESS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ACCESS):
    case SUCCESS(ACTION_TYPES.UPDATE_ACCESS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ACCESS):
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

const apiUrl = 'api/accesses';

// Actions

export const getEntities: ICrudGetAllAction<IAccess> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ACCESS_LIST,
    payload: axios.get<IAccess>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IAccess> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ACCESS,
    payload: axios.get<IAccess>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAccess> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ACCESS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAccess> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ACCESS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAccess> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ACCESS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
