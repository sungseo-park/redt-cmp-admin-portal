import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IStarGate, defaultValue } from 'app/shared/model/star-gate.model';

export const ACTION_TYPES = {
  FETCH_STARGATE_LIST: 'starGate/FETCH_STARGATE_LIST',
  FETCH_STARGATE: 'starGate/FETCH_STARGATE',
  CREATE_STARGATE: 'starGate/CREATE_STARGATE',
  UPDATE_STARGATE: 'starGate/UPDATE_STARGATE',
  DELETE_STARGATE: 'starGate/DELETE_STARGATE',
  RESET: 'starGate/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IStarGate>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type StarGateState = Readonly<typeof initialState>;

// Reducer

export default (state: StarGateState = initialState, action): StarGateState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_STARGATE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_STARGATE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_STARGATE):
    case REQUEST(ACTION_TYPES.UPDATE_STARGATE):
    case REQUEST(ACTION_TYPES.DELETE_STARGATE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_STARGATE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_STARGATE):
    case FAILURE(ACTION_TYPES.CREATE_STARGATE):
    case FAILURE(ACTION_TYPES.UPDATE_STARGATE):
    case FAILURE(ACTION_TYPES.DELETE_STARGATE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_STARGATE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_STARGATE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_STARGATE):
    case SUCCESS(ACTION_TYPES.UPDATE_STARGATE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_STARGATE):
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

const apiUrl = 'api/star-gates';

// Actions

export const getEntities: ICrudGetAllAction<IStarGate> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_STARGATE_LIST,
    payload: axios.get<IStarGate>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IStarGate> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_STARGATE,
    payload: axios.get<IStarGate>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IStarGate> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_STARGATE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IStarGate> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_STARGATE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IStarGate> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_STARGATE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
