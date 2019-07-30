import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from './user-management';
// prettier-ignore
import starGate, {
  StarGateState
} from 'app/entities/star-gate/star-gate.reducer';
// prettier-ignore
import costManagementPlatform, {
  CostManagementPlatformState
} from 'app/entities/cost-management-platform/cost-management-platform.reducer';
// prettier-ignore
import honestBuilding, {
  HonestBuildingState
} from 'app/entities/honest-building/honest-building.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly starGate: StarGateState;
  readonly costManagementPlatform: CostManagementPlatformState;
  readonly honestBuilding: HonestBuildingState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  starGate,
  costManagementPlatform,
  honestBuilding,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
