import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './honest-building.reducer';
import { IHonestBuilding } from 'app/shared/model/honest-building.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHonestBuildingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class HonestBuildingDetail extends React.Component<IHonestBuildingDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { honestBuildingEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="cmpAdminPortalApp.honestBuilding.detail.title">HonestBuilding</Translate> [
            <b>{honestBuildingEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="userid">
                <Translate contentKey="cmpAdminPortalApp.honestBuilding.userid">UserID</Translate>
              </span>
            </dt>
            <dt>
              <span id="username">
                <Translate contentKey="cmpAdminPortalApp.honestBuilding.username">UserName</Translate>
              </span>
            </dt>
            <dd>{honestBuildingEntity.username}</dd>
            <dt>
              <span id="access">
                <Translate contentKey="cmpAdminPortalApp.honestBuilding.access">Access</Translate>
              </span>
            </dt>
            <dd>{honestBuildingEntity.access}</dd>
          </dl>
          <Button tag={Link} to="/entity/honest-building" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/honest-building/${honestBuildingEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ honestBuilding }: IRootState) => ({
  honestBuildingEntity: honestBuilding.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(HonestBuildingDetail);
