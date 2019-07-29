import { IHB } from 'app/shared/model/hb.model';
import { IStarGate } from 'app/shared/model/star-gate.model';

export const enum Access {
  FULL = 'FULL',
  LIMITED = 'LIMITED'
}

export interface ICostManagementPlatform {
  id?: number;
  role?: string;
  access?: Access;
  hb?: IHB;
  stargate?: IStarGate;
}

export const defaultValue: Readonly<ICostManagementPlatform> = {};
