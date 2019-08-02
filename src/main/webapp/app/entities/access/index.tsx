import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Access from './access';
import AccessDetail from './access-detail';
import AccessUpdate from './access-update';
import AccessDeleteDialog from './access-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AccessUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AccessUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AccessDetail} />
      <ErrorBoundaryRoute path={match.url} component={Access} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={AccessDeleteDialog} />
  </>
);

export default Routes;
