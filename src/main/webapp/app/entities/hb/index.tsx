import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HB from './hb';
import HBDetail from './hb-detail';
import HBUpdate from './hb-update';
import HBDeleteDialog from './hb-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HBUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HBUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HBDetail} />
      <ErrorBoundaryRoute path={match.url} component={HB} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={HBDeleteDialog} />
  </>
);

export default Routes;
