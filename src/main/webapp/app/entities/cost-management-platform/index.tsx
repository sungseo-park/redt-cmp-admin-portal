import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CostManagementPlatform from './cost-management-platform';
import CostManagementPlatformDetail from './cost-management-platform-detail';
import CostManagementPlatformUpdate from './cost-management-platform-update';
import CostManagementPlatformDeleteDialog from './cost-management-platform-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CostManagementPlatformUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CostManagementPlatformUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CostManagementPlatformDetail} />
      <ErrorBoundaryRoute path={match.url} component={CostManagementPlatform} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CostManagementPlatformDeleteDialog} />
  </>
);

export default Routes;
