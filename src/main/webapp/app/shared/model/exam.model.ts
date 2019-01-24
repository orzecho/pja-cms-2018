import { IExamResult } from 'app/shared/model//exam-result.model';
import { IWord } from 'app/shared/model//word.model';

export const enum TestType {
    WRITTEN_MIXED = 'WRITTEN_MIXED',
    WRITTEN_PL = 'WRITTEN_PL',
    WRITTEN_KANA = 'WRITTEN_KANA',
    WRITTEN_KANJI = 'WRITTEN_KANJI'
}

export interface IExam {
    id?: number;
    name?: string;
    type?: TestType;
    code?: string;
    results?: IExamResult[];
    creatorLogin?: string;
    creatorId?: number;
    words?: IWord[];
}

export class Exam implements IExam {
    constructor(
        public id?: number,
        public name?: string,
        public type?: TestType,
        public code?: string,
        public results: IExamResult[] = [],
        public creatorLogin?: string,
        public creatorId?: number,
        public words: IWord[] = []
    ) {}
}
