import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './role.reducer';
import { IRole } from 'app/shared/model/role.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRoleDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RoleDetail extends React.Component<IRoleDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { roleEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="cmpAdminPortalApp.role.detail.title">Role</Translate> [<b>{roleEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="role">
                <Translate contentKey="cmpAdminPortalApp.role.role">Role</Translate>
              </span>
            </dt>
            <dd>{roleEntity.role}</dd>
            <dt>
              <span id="accessId">
                <Translate contentKey="cmpAdminPortalApp.role.accessId">Access Id</Translate>
              </span>
            </dt>
            <dd>{roleEntity.accessId}</dd>
            <dt>
              <span id="roleId">
                <Translate contentKey="cmpAdminPortalApp.role.roleId">Role Id</Translate>
              </span>
            </dt>
            <dd>{roleEntity.roleId}</dd>
          </dl>
          <Button tag={Link} to="/entity/role" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/role/${roleEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ role }: IRootState) => ({
  roleEntity: role.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RoleDetail);
