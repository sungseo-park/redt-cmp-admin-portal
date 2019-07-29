import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import StarGate from './star-gate';
import CostManagementPlatform from './cost-management-platform';
import HB from './hb';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/star-gate`} component={StarGate} />
      <ErrorBoundaryRoute path={`${match.url}/cost-management-platform`} component={CostManagementPlatform} />
      <ErrorBoundaryRoute path={`${match.url}/hb`} component={HB} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
