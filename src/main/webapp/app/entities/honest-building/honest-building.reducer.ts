import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IHonestBuilding, defaultValue } from 'app/shared/model/honest-building.model';

export const ACTION_TYPES = {
  FETCH_HONESTBUILDING_LIST: 'honestBuilding/FETCH_HONESTBUILDING_LIST',
  FETCH_HONESTBUILDING: 'honestBuilding/FETCH_HONESTBUILDING',
  CREATE_HONESTBUILDING: 'honestBuilding/CREATE_HONESTBUILDING',
  UPDATE_HONESTBUILDING: 'honestBuilding/UPDATE_HONESTBUILDING',
  DELETE_HONESTBUILDING: 'honestBuilding/DELETE_HONESTBUILDING',
  RESET: 'honestBuilding/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IHonestBuilding>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type HonestBuildingState = Readonly<typeof initialState>;

// Reducer

export default (state: HonestBuildingState = initialState, action): HonestBuildingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_HONESTBUILDING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_HONESTBUILDING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_HONESTBUILDING):
    case REQUEST(ACTION_TYPES.UPDATE_HONESTBUILDING):
    case REQUEST(ACTION_TYPES.DELETE_HONESTBUILDING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_HONESTBUILDING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_HONESTBUILDING):
    case FAILURE(ACTION_TYPES.CREATE_HONESTBUILDING):
    case FAILURE(ACTION_TYPES.UPDATE_HONESTBUILDING):
    case FAILURE(ACTION_TYPES.DELETE_HONESTBUILDING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_HONESTBUILDING_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_HONESTBUILDING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_HONESTBUILDING):
    case SUCCESS(ACTION_TYPES.UPDATE_HONESTBUILDING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_HONESTBUILDING):
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

const apiUrl = 'api/honest-buildings';

// Actions

export const getEntities: ICrudGetAllAction<IHonestBuilding> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_HONESTBUILDING_LIST,
    payload: axios.get<IHonestBuilding>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IHonestBuilding> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_HONESTBUILDING,
    payload: axios.get<IHonestBuilding>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IHonestBuilding> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_HONESTBUILDING,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IHonestBuilding> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_HONESTBUILDING,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IHonestBuilding> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_HONESTBUILDING,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
