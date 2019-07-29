export const enum Access {
  FULL = 'FULL',
  LIMITED = 'LIMITED'
}

export interface IHonestBuilding {
  id?: number;
  role?: string;
  access?: Access;
}

export const defaultValue: Readonly<IHonestBuilding> = {};
