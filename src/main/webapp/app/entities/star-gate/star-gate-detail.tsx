import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './star-gate.reducer';
import { IStarGate } from 'app/shared/model/star-gate.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStarGateDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class StarGateDetail extends React.Component<IStarGateDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { starGateEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="cmpAdminPortalApp.starGate.detail.title">StarGate</Translate> [<b>{starGateEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="role">
                <Translate contentKey="cmpAdminPortalApp.starGate.role">Role</Translate>
              </span>
            </dt>
            <dd>{starGateEntity.role}</dd>
          </dl>
          <Button tag={Link} to="/entity/star-gate" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/star-gate/${starGateEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ starGate }: IRootState) => ({
  starGateEntity: starGate.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(StarGateDetail);
