import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './cost-management-platform.reducer';
import { ICostManagementPlatform } from 'app/shared/model/cost-management-platform.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICostManagementPlatformDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CostManagementPlatformDetail extends React.Component<ICostManagementPlatformDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { costManagementPlatformEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.detail.title">CostManagementPlatform</Translate> [
            <b>{costManagementPlatformEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="role">
                <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.role">Role</Translate>
              </span>
            </dt>
            <dd>{costManagementPlatformEntity.role}</dd>
            <dt>
              <span id="access">
                <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.access">Access</Translate>
              </span>
            </dt>
            <dd>{costManagementPlatformEntity.access}</dd>
            {/*<dt>*/}
            {/*  <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.honestbuilding">Honestbuilding</Translate>*/}
            {/*</dt>*/}
            {/*<dd>{costManagementPlatformEntity.honestbuilding ? costManagementPlatformEntity.honestbuilding.role : ''}</dd>*/}
            {/*<dt>*/}
            {/*  <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.honestbuilding">Honestbuilding</Translate>*/}
            {/*</dt>*/}
            {/*<dd>{costManagementPlatformEntity.honestbuilding ? costManagementPlatformEntity.honestbuilding.access : ''}</dd>*/}
            {/*<dt>*/}
            {/*  <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.stargate">Stargate</Translate>*/}
            {/*</dt>*/}
            {/*<dd>{costManagementPlatformEntity.stargate ? costManagementPlatformEntity.stargate.role : ''}</dd>*/}
          </dl>
          <Button tag={Link} to="/entity/cost-management-platform" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/cost-management-platform/${costManagementPlatformEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ costManagementPlatform }: IRootState) => ({
  costManagementPlatformEntity: costManagementPlatform.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CostManagementPlatformDetail);
