export const enum Access {
  FULL = 'FULL',
  LIMITED = 'LIMITED'
}

export interface IHonestBuilding {
  id?: number;
  userid?: string;
  username?: string;
  access?: string;
}

export const defaultValue: Readonly<IHonestBuilding> = {};
