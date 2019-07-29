import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './hb.reducer';
import { IHB } from 'app/shared/model/hb.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHBDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class HBDetail extends React.Component<IHBDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { hBEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="cmpAdminPortalApp.hB.detail.title">HB</Translate> [<b>{hBEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="role">
                <Translate contentKey="cmpAdminPortalApp.hB.role">Role</Translate>
              </span>
            </dt>
            <dd>{hBEntity.role}</dd>
            <dt>
              <span id="access">
                <Translate contentKey="cmpAdminPortalApp.hB.access">Access</Translate>
              </span>
            </dt>
            <dd>{hBEntity.access}</dd>
          </dl>
          <Button tag={Link} to="/entity/hb" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/hb/${hBEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ hB }: IRootState) => ({
  hBEntity: hB.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(HBDetail);
