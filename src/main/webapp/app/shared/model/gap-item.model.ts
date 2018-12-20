export interface IGapItem {
    id?: number;
    key?: string;
    value?: string;
    testItemId?: number;
    answerFromUser?: string;
    answerCorrect?: boolean;
}

export class GapItem implements IGapItem {
    constructor(
        public id?: number,
        public key?: string,
        public value?: string,
        public testItemId?: number,
        public answerFromUser?: string,
        public answerCorrect?: boolean
    ) {}
}
