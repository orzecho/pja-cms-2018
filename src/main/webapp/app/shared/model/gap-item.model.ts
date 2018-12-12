export interface IGapItem {
    id?: number;
    key?: string;
    value?: string;
    testItemId?: number;
}

export class GapItem implements IGapItem {
    constructor(public id?: number, public key?: string, public value?: string, public testItemId?: number) {}
}
