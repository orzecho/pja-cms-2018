/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NyanTestModule } from '../../../test.module';
import { WordsTestUpdateComponent } from 'app/entities/words-test/words-test-update.component';
import { WordsTestService } from 'app/entities/words-test/words-test.service';
import { WordsTest } from 'app/shared/model/words-test.model';

describe('Component Tests', () => {
    describe('WordsTest Management Update Component', () => {
        let comp: WordsTestUpdateComponent;
        let fixture: ComponentFixture<WordsTestUpdateComponent>;
        let service: WordsTestService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [WordsTestUpdateComponent]
            })
                .overrideTemplate(WordsTestUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(WordsTestUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WordsTestService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new WordsTest(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.wordsTest = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity, undefined);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new WordsTest();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.wordsTest = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity, undefined);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
