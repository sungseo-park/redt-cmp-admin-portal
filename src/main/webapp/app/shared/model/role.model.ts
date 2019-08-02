export const enum Access {
  FULL = 'FULL',
  LIMITED = 'LIMITED'
}

export interface IRole {
  id?: number;
  role?: string;
  access?: Access;
}

export const defaultValue: Readonly<IRole> = {};
