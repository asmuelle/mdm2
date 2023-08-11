import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ScopeDetailComponent } from './scope-detail.component';

describe('Scope Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ScopeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ScopeDetailComponent,
              resolve: { scope: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(ScopeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load scope on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ScopeDetailComponent);

      // THEN
      expect(instance.scope).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
