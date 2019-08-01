import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IHonestBuilding } from 'app/shared/model/honest-building.model';
import { getEntities as getHonestBuildings } from 'app/entities/honest-building/honest-building.reducer';
import { IStarGate } from 'app/shared/model/star-gate.model';
import { getEntities as getStarGates } from 'app/entities/star-gate/star-gate.reducer';
import { getEntity, updateEntity, createEntity, reset } from './cost-management-platform.reducer';
import { ICostManagementPlatform } from 'app/shared/model/cost-management-platform.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICostManagementPlatformUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICostManagementPlatformUpdateState {
  isNew: boolean;
  honestbuildingId: string;
  stargateId: string;
}

export class CostManagementPlatformUpdate extends React.Component<ICostManagementPlatformUpdateProps, ICostManagementPlatformUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      honestbuildingId: '0',
      stargateId: '0',
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

    this.props.getHonestBuildings();
    this.props.getStarGates();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { costManagementPlatformEntity } = this.props;
      const entity = {
        ...costManagementPlatformEntity,
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
    this.props.history.push('/entity/cost-management-platform');
  };

  render() {
    const { costManagementPlatformEntity, honestBuildings, starGates, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            {!isNew ? (
              <h2 id="cmpAdminPortalApp.costManagementPlatform.home.editAccess">
                <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.home.editAccess">Edit a Access</Translate>
              </h2>
            ) : (
              <h2 id="cmpAdminPortalApp.costManagementPlatform.home.createRole">
                <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.home.createRole">Create a new Role</Translate>
              </h2>
            )}
            {/*<h2 id="cmpAdminPortalApp.costManagementPlatform.home.createOrEditLabel">*/}
            {/*  <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.home.createOrEditLabel">*/}
            {/*    Create or edit a CostManagementPlatform*/}
            {/*  </Translate>*/}
            {/*</h2>*/}
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : costManagementPlatformEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  // Edit Mode
                  <div>
                    <AvGroup>
                      <Label for="cost-management-platform-id">
                        <Translate contentKey="global.field.id">ID</Translate>
                      </Label>
                      <AvInput id="cost-management-platform-id" type="text" className="form-control" name="id" required readOnly />
                    </AvGroup>
                    <AvGroup>
                      <Label id="useridLabel" for="cost-management-platform-userid">
                        <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.userid">Userid</Translate>
                      </Label>
                      <AvInput id="cost-management-platform-userid" type="text" name="userid" required readOnly />
                      {/*<AvField id="cost-management-platform-userid" type="text" name="userid" />*/}
                    </AvGroup>
                    <AvGroup>
                      <Label id="usernameLabel" for="cost-management-platform-username">
                        <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.username">UserName</Translate>
                      </Label>
                      <AvInput id="cost-management-platform-username" type="text" name="username" required readOnly />
                      {/*<AvField id="cost-management-platform-username" type="text" name="username" />*/}
                    </AvGroup>
                    <AvGroup>
                      <Label id="projectidLabel" for="cost-management-platform-projectid">
                        <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.projectid">projectid</Translate>
                      </Label>
                      <AvInput id="cost-management-platform-projectid" type="text" name="projectid" required readOnly />
                      {/*<AvField id="cost-management-platform-projectid" type="text" name="projectid" />*/}
                    </AvGroup>
                    <AvGroup>
                      <Label id="roleLabel" for="cost-management-platform-role">
                        <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.role">Role</Translate>
                      </Label>
                      <AvInput id="cost-management-platform-role" type="text" name="role" required readOnly />
                      {/*<AvField id="cost-management-platform-role" type="text" name="role" />*/}
                    </AvGroup>
                    <AvGroup>
                      <Label id="accessLabel" for="cost-management-platform-access">
                        <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.access">Access</Translate>
                      </Label>
                      <AvInput
                        id="cost-management-platform-access"
                        type="select"
                        className="form-control"
                        name="access"
                        value={(!isNew && costManagementPlatformEntity.access) || 'LIMITED'}
                      >
                        <option value="LIMITED">{translate('cmpAdminPortalApp.Access.LIMITED')}</option>
                        <option value="FULL">{translate('cmpAdminPortalApp.Access.FULL')}</option>
                      </AvInput>
                    </AvGroup>
                  </div>
                ) : (
                  // New Mode
                  <div>
                    <AvGroup>
                      <Label id="roleLabel" for="cost-management-platform-role">
                        <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.role">Role</Translate>
                      </Label>
                      <AvField id="cost-management-platform-role" type="text" name="role" />
                    </AvGroup>
                    <AvGroup>
                      <Label id="hbId" for="cost-management-platform-hbId">
                        <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.hbId">hbId</Translate>
                      </Label>
                      <AvInput id="cost-management-platform-hbId" type="text" name="hbId" required readOnly />
                    </AvGroup>
                    <AvGroup>
                      <Label id="accessLabel" for="cost-management-platform-access">
                        <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.access">Access</Translate>
                      </Label>
                      <AvInput
                        id="cost-management-platform-access"
                        type="select"
                        className="form-control"
                        name="access"
                        value={(!isNew && costManagementPlatformEntity.access) || 'LIMITED'}
                      >
                        <option value="LIMITED">{translate('cmpAdminPortalApp.Access.LIMITED')}</option>
                        <option value="FULL">{translate('cmpAdminPortalApp.Access.FULL')}</option>
                      </AvInput>
                    </AvGroup>
                  </div>
                )}
                {/*<AvGroup>*/}
                {/*  <Label id="accessLabel" for="cost-management-platform-access">*/}
                {/*    <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.access">Access</Translate>*/}
                {/*  </Label>*/}
                {/*  <AvInput*/}
                {/*    id="cost-management-platform-access"*/}
                {/*    type="select"*/}
                {/*    className="form-control"*/}
                {/*    name="access"*/}
                {/*    value={(!isNew && costManagementPlatformEntity.access) || 'LIMITED'}*/}
                {/*  >*/}
                {/*    <option value="LIMITED">{translate('cmpAdminPortalApp.Access.LIMITED')}</option>*/}
                {/*    <option value="FULL">{translate('cmpAdminPortalApp.Access.FULL')}</option>*/}
                {/*  </AvInput>*/}
                {/*</AvGroup>*/}
                {/*<AvGroup>*/}
                {/*  <Label for="cost-management-platform-honestbuilding">*/}
                {/*    <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.honestbuilding">Honestbuilding</Translate>*/}
                {/*  </Label>*/}
                {/*  <AvInput id="cost-management-platform-honestbuilding" type="select" className="form-control" name="honestbuilding.id">*/}
                {/*    <option value="" key="0" />*/}
                {/*    {honestBuildings*/}
                {/*      ? honestBuildings.map(otherEntity => (*/}
                {/*          <option value={otherEntity.id} key={otherEntity.id}>*/}
                {/*            {otherEntity.role}*/}
                {/*          </option>*/}
                {/*        ))*/}
                {/*      : null}*/}
                {/*  </AvInput>*/}
                {/*</AvGroup>*/}
                {/*<AvGroup>*/}
                {/*  <Label for="cost-management-platform-honestbuilding">*/}
                {/*    <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.honestbuilding">Honestbuilding</Translate>*/}
                {/*  </Label>*/}
                {/*  <AvInput id="cost-management-platform-honestbuilding" type="select" className="form-control" name="honestbuilding.id">*/}
                {/*    <option value="" key="0" />*/}
                {/*    {honestBuildings*/}
                {/*      ? honestBuildings.map(otherEntity => (*/}
                {/*          <option value={otherEntity.id} key={otherEntity.id}>*/}
                {/*            {otherEntity.access}*/}
                {/*          </option>*/}
                {/*        ))*/}
                {/*      : null}*/}
                {/*  </AvInput>*/}
                {/*</AvGroup>*/}
                {/*<AvGroup>*/}
                {/*  <Label for="cost-management-platform-stargate">*/}
                {/*    <Translate contentKey="cmpAdminPortalApp.costManagementPlatform.stargate">Stargate</Translate>*/}
                {/*  </Label>*/}
                {/*  <AvInput id="cost-management-platform-stargate" type="select" className="form-control" name="stargate.id">*/}
                {/*    <option value="" key="0" />*/}
                {/*    {starGates*/}
                {/*      ? starGates.map(otherEntity => (*/}
                {/*          <option value={otherEntity.id} key={otherEntity.id}>*/}
                {/*            {otherEntity.role}*/}
                {/*          </option>*/}
                {/*        ))*/}
                {/*      : null}*/}
                {/*  </AvInput>*/}
                {/*</AvGroup>*/}
                <Button tag={Link} id="cancel-save" to="/entity/cost-management-platform" replace color="info">
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
  honestBuildings: storeState.honestBuilding.entities,
  starGates: storeState.starGate.entities,
  costManagementPlatformEntity: storeState.costManagementPlatform.entity,
  loading: storeState.costManagementPlatform.loading,
  updating: storeState.costManagementPlatform.updating,
  updateSuccess: storeState.costManagementPlatform.updateSuccess
});

const mapDispatchToProps = {
  getHonestBuildings,
  getStarGates,
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
)(CostManagementPlatformUpdate);
