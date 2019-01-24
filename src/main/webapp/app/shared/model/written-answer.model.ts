import { IWord } from 'app/shared/model/word.model';

export interface IWrittenAnswer {
    id?: number;
    translationFrom?: string;
    translation?: string;
    kana?: string;
    kanji?: string;
    isRightAnswer?: boolean;
    word?: IWord;
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
        public word?: IWord,
        public examId?: number
    ) {
        this.isRightAnswer = this.isRightAnswer || false;
    }
}
