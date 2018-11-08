import { ILesson } from 'app/shared/model//lesson.model';
import { IWord } from 'app/shared/model//word.model';

export interface ITag {
    id?: number;
    name?: string;
    lessons?: ILesson[];
    words?: IWord[];
}

export class Tag implements ITag {
    constructor(public id?: number, public name?: string, public lessons?: ILesson[], public words?: IWord[]) {}
}
