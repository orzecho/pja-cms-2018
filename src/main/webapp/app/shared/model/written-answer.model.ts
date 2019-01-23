export interface IWrittenAnswer {
    id?: number;
    translationFrom?: string;
    translation?: string;
    kana?: string;
    kanji?: string;
    isRightAnswer?: boolean;
    wordId?: number;
    examId?: number;
}

export class WrittenAnswer implements IWrittenAnswer {
    constructor(
        public id?: number,
        public translationFrom?: string,
        public translation?: string,
        public kana?: string,
        public kanji?: string,
        public isRightAnswer?: boolean,
        public wordId?: number,
        public examId?: number
    ) {
        this.isRightAnswer = this.isRightAnswer || false;
    }
}
