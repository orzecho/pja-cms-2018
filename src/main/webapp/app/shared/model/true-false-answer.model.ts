export interface ITrueFalseAnswer {
    id?: number;
    translationFrom?: string;
    isRightAnswer?: boolean;
    srcWordId?: number;
    targetWordId?: number;
    examId?: number;
}

export class TrueFalseAnswer implements ITrueFalseAnswer {
    constructor(
        public id?: number,
        public translationFrom?: string,
        public isRightAnswer?: boolean,
        public srcWordId?: number,
        public targetWordId?: number,
        public examId?: number
    ) {
        this.isRightAnswer = this.isRightAnswer || false;
    }
}
