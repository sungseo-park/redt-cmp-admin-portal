import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate, ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ICostManagementPlatform } from 'app/shared/model/cost-management-platform.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './cost-management-platform.reducer';

export interface ICostManagementPlatformDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CostManagementPlatformDeleteDialog extends React.Component<ICostManagementPlatformDeleteDialogProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  confirmDelete = event => {
    this.props.deleteEntity(this.props.costManagementPlatformEntity.id);
    this.handleClose(event);
  };

  handleClose = event => {
    event.stopPropagation();
    this.props.history.goBack();
  };

  render() {
    const { costManagementPlatformEntity } = this.props;
    return (
      <Modal isOpen toggle={this.handleClose}>
        <ModalHeader toggle={this.handleClose}>
          <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
        </ModalHeader>
        <ModalBody id="cmpAdminPortalApp.costManagementPlatform.delete.question">
          <Translate
            contentKey="cmpAdminPortalApp.costManagementPlatform.delete.question"
            interpolate={{ id: costManagementPlatformEntity.id }}
          >
            Are you sure you want to delete this CostManagementPlatform?
          </Translate>
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={this.handleClose}>
            <FontAwesomeIcon icon="ban" />
            &nbsp;
            <Translate contentKey="entity.action.cancel">Cancel</Translate>
          </Button>
          <Button id="jhi-confirm-delete-costManagementPlatform" color="danger" onClick={this.confirmDelete}>
            <FontAwesomeIcon icon="trash" />
            &nbsp;
            <Translate contentKey="entity.action.delete">Delete</Translate>
          </Button>
        </ModalFooter>
      </Modal>
    );
  }
}

const mapStateToProps = ({ costManagementPlatform }: IRootState) => ({
  costManagementPlatformEntity: costManagementPlatform.entity
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CostManagementPlatformDeleteDialog);
