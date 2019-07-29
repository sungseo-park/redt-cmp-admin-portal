import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './honest-building.reducer';
import { IHonestBuilding } from 'app/shared/model/honest-building.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IHonestBuildingUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IHonestBuildingUpdateState {
  isNew: boolean;
}

export class HonestBuildingUpdate extends React.Component<IHonestBuildingUpdateProps, IHonestBuildingUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { honestBuildingEntity } = this.props;
      const entity = {
        ...honestBuildingEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/honest-building');
  };

  render() {
    const { honestBuildingEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="cmpAdminPortalApp.honestBuilding.home.createOrEditLabel">
              <Translate contentKey="cmpAdminPortalApp.honestBuilding.home.createOrEditLabel">Create or edit a HonestBuilding</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : honestBuildingEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="honest-building-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="honest-building-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="roleLabel" for="honest-building-role">
                    <Translate contentKey="cmpAdminPortalApp.honestBuilding.role">Role</Translate>
                  </Label>
                  <AvField id="honest-building-role" type="text" name="role" />
                </AvGroup>
                <AvGroup>
                  <Label id="accessLabel" for="honest-building-access">
                    <Translate contentKey="cmpAdminPortalApp.honestBuilding.access">Access</Translate>
                  </Label>
                  <AvInput
                    id="honest-building-access"
                    type="select"
                    className="form-control"
                    name="access"
                    value={(!isNew && honestBuildingEntity.access) || 'FULL'}
                  >
                    <option value="FULL">{translate('cmpAdminPortalApp.Access.FULL')}</option>
                    <option value="LIMITED">{translate('cmpAdminPortalApp.Access.LIMITED')}</option>
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/honest-building" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  honestBuildingEntity: storeState.honestBuilding.entity,
  loading: storeState.honestBuilding.loading,
  updating: storeState.honestBuilding.updating,
  updateSuccess: storeState.honestBuilding.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(HonestBuildingUpdate);
