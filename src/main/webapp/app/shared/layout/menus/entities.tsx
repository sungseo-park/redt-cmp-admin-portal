import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name={translate('global.menu.entities.main')} id="entity-menu">
    <MenuItem icon="asterisk" to="/entity/star-gate">
      <Translate contentKey="global.menu.entities.starGate" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/cost-management-platform">
      <Translate contentKey="global.menu.entities.costManagementPlatform" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/honest-building">
      <Translate contentKey="global.menu.entities.honestBuilding" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
