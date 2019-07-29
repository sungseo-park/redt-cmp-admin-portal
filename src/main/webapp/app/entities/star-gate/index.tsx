import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import StarGate from './star-gate';
import StarGateDetail from './star-gate-detail';
import StarGateUpdate from './star-gate-update';
import StarGateDeleteDialog from './star-gate-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={StarGateUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={StarGateUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={StarGateDetail} />
      <ErrorBoundaryRoute path={match.url} component={StarGate} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={StarGateDeleteDialog} />
  </>
);

export default Routes;
