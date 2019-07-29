import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HonestBuilding from './honest-building';
import HonestBuildingDetail from './honest-building-detail';
import HonestBuildingUpdate from './honest-building-update';
import HonestBuildingDeleteDialog from './honest-building-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HonestBuildingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HonestBuildingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HonestBuildingDetail} />
      <ErrorBoundaryRoute path={match.url} component={HonestBuilding} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={HonestBuildingDeleteDialog} />
  </>
);

export default Routes;
