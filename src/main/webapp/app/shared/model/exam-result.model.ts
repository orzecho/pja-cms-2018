import { Moment } from 'moment';
import { IWrittenAnswer } from 'app/shared/model//written-answer.model';
import { ITrueFalseAnswer } from 'app/shared/model//true-false-answer.model';

export interface IExamResult {
    id?: number;
    date?: Moment;
    result?: number;
    writtenAnswers?: IWrittenAnswer[];
    trueFalseAnswers?: ITrueFalseAnswer[];
    studentLogin?: string;
    studentId?: number;
    examId?: number;
}

export class ExamResult implements IExamResult {
    constructor(
        public id?: number,
        public date?: Moment,
        public result?: number,
        public writtenAnswers?: IWrittenAnswer[],
        public trueFalseAnswers?: ITrueFalseAnswer[],
        public studentLogin?: string,
        public studentId?: number,
        public examId?: number
    ) {}
}
